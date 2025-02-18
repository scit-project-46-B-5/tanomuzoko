package org.scit.project.recipe.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class RecipeRecommendConfig {

    @Value("${openai.api.key}") // application.properties에 저장된 openai.api.key를 불러온다.
    private String openAiKey;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl("https://api.openai.com")
                .defaultHeader("Authorization", "Bearer " + openAiKey)
                .build();
    }
    
    // ObjectMapper 빈 등록

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
