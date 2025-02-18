package org.scit.project.recipe.service;

import java.util.List;

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

@Service
@Transactional
@RequiredArgsConstructor
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
    public String sendRequestToChatGPT(RecipeUserRequestDTO recipeUserRequestDTO) {

        List<MessageChatGPTDTO> messages = List.of(MessageChatGPTDTO.TOUSERMESSAGE(recipeUserRequestDTO),
                MessageChatGPTDTO.TOSYSTEMMESSAGE(recipeUserRequestDTO));
        RecipeChatGPTRequestDTO recipeChatGPTRequestDTO = RecipeChatGPTRequestDTO.TODTO(model, messages, temperature);

        // //GPT-4
        try {
            String responseFromChatGPT = webClient.post()
                    .uri("https://api.openai.com/v1/chat/completions")
                    .header("Authorization", "Bearer " + apiKey)
                    .bodyValue(recipeChatGPTRequestDTO)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            String root = objectMapper.writeValueAsString(responseFromChatGPT);

            return root;
        } catch (Exception exception) {
            exception.printStackTrace();
            //dummydata
            //String root = objectMapper.writeValueAsString(responseFromChatGPT);
            return "";
        }

        // webClient.post()
    }
}
