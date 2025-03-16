package org.scit.project.recipe.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.scit.project.recipe.dto.MessageChatGPTDTO;
import org.scit.project.recipe.dto.RecipeChatGPTRequestDTO;
import org.scit.project.recipe.dto.RecipeConditionDTO;
import org.scit.project.recipe.dto.RecipeProjection;
import org.scit.project.recipe.dto.RecipeUserRequestDTO;
import org.scit.project.recipe.dto.RecipeUserResponseDTO;
import org.scit.project.recipe.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecipeService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final RecipeRepository recipeRepository;

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.model}")
    private String model;

    @Value("${openai.api.temperature}")
    private float temperature;

    /**
     * recipe 정보를 chatGPT에 요청해 받아와 HTML에 맞게 parsing한 값을 return
     * @param recipeUserRequestDTO
     * @return
     */
    public RecipeUserResponseDTO getRecipeResponse(RecipeUserRequestDTO recipeUserRequestDTO) {
        //CHATGPT에 필요한 system, user message를 생성
        RecipeChatGPTRequestDTO recipeChatGPTRequestDTO = createMessageForChatGPTRequest(recipeUserRequestDTO);

        try {
            String jsonString = getResponseFromChatGPT(recipeChatGPTRequestDTO);
            RecipeUserResponseDTO recipeUserResponseDTO = parseResponseFromChatGPT(jsonString, recipeUserRequestDTO.getRecipeConditionDTO());
                
            return recipeUserResponseDTO;

        } catch (Exception exception) {
            throw new RuntimeException("알 수 없는 에러가 발생하였습니다");
        }
    }

    public List<RecipeProjection> getAllRecipesByUser(Long userSeq) {
        List<RecipeProjection> recipes = recipeRepository.findRecipesByUser(userSeq);
        
        return recipes;
    }


    private RecipeChatGPTRequestDTO createMessageForChatGPTRequest(RecipeUserRequestDTO recipeUserRequestDTO) {
        List<MessageChatGPTDTO> messages = List.of(MessageChatGPTDTO.TOUSERMESSAGE(recipeUserRequestDTO),
                MessageChatGPTDTO.TOSYSTEMMESSAGE(recipeUserRequestDTO));

        return RecipeChatGPTRequestDTO.TODTO(model, messages, temperature);
    }

    /**
     * 타이틀, 재료, 조리방법을 parsing. 조리방법은 숫자를 기준으로 parsing.
     */
    private RecipeUserResponseDTO parseResponseFromChatGPT(String jsonString, RecipeConditionDTO recipeUserRequestDTO) throws JsonMappingException, JsonProcessingException {
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

            return RecipeUserResponseDTO.TODTO(title, ingredients, cookingMethods, recipeUserRequestDTO);
        } else {
            throw new RuntimeException("choiesNode가 array형태가 아닙니다.");
        }
    }

    private String getResponseFromChatGPT(RecipeChatGPTRequestDTO recipeChatGPTRequestDTO) {
        return webClient.post()
                        .uri("https://api.openai.com/v1/chat/completions")
                        .header("Authorization", "Bearer " + apiKey)
                        .bodyValue(recipeChatGPTRequestDTO)
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();
    }
}
