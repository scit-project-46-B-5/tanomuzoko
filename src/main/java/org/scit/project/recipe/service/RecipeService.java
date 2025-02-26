package org.scit.project.recipe.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.scit.project.recipe.dto.MessageChatGPTDTO;
import org.scit.project.recipe.dto.RecipeChatGPTRequestDTO;
import org.scit.project.recipe.dto.RecipeUserRequestDTO;
import org.scit.project.recipe.dto.RecipeUserResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class RecipeService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.model}")
    private String model;

    @Value("${openai.api.temperature}")
    private float temperature;

    // model:
    public RecipeUserResponseDTO sendRequestToChatGPT(RecipeUserRequestDTO recipeUserRequestDTO) {

        List<MessageChatGPTDTO> messages = List.of(MessageChatGPTDTO.TOUSERMESSAGE(recipeUserRequestDTO),
                MessageChatGPTDTO.TOSYSTEMMESSAGE(recipeUserRequestDTO));
        RecipeChatGPTRequestDTO recipeChatGPTRequestDTO = RecipeChatGPTRequestDTO.TODTO(model, messages, temperature);

        // //GPT-4
        try {
            String jsonString = webClient.post()
                    .uri("https://api.openai.com/v1/chat/completions")
                    .header("Authorization", "Bearer " + apiKey)
                    .bodyValue(recipeChatGPTRequestDTO)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            JsonNode jsonNode = objectMapper.readTree(jsonString);

            JsonNode choicesNode = jsonNode.get("choices");
            if (choicesNode.isArray()) {
                String content = jsonNode.get("choices").get(0).get("message").get("content").asText();
                String firstElement = "타이틀: ";
                String secondElement = "재료: ";
                String thridElement = "조리방법: ";
                String title = content.substring(content.indexOf(firstElement) + firstElement.length(), content.indexOf(secondElement));
                String[] ingredients = content.substring(content.lastIndexOf(secondElement) + secondElement.length(), content.indexOf(thridElement)).split(",");
                String cookingMethod = content.substring(content.lastIndexOf(thridElement) + thridElement.length());

                Pattern pattern = Pattern.compile("\\d+\\.\\s*(.*?)(?=\\s*\\d+\\.|$)");
                Matcher matcher = pattern.matcher(cookingMethod);
                List<String> steps = new ArrayList<>();
                while (matcher.find()) {
                    steps.add(matcher.group(1).trim());
                }
                String[] cookingMethods = steps.toArray(new String[0]);
                return RecipeUserResponseDTO.TODTO(title, ingredients, cookingMethods, 
                                                            recipeUserRequestDTO.getRecipeConditionDTO()
                                                    );
            } else {
                throw new RuntimeException("choiesNode가 array형태가 아닙니다.");
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            throw new RuntimeException("알 수 없는 에러가 발생하였습니다");
        }

        // webClient.post()
    }
}
