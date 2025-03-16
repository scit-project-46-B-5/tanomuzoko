package org.scit.project.recipe.dto;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Builder
@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecipeChatGPTRequestDTO {

    private String model;
    private List<MessageChatGPTDTO> messages;
    private float temperature;

    public static RecipeChatGPTRequestDTO TODTO(String model, List<MessageChatGPTDTO> messages, float temperature) {
      return RecipeChatGPTRequestDTO.builder()
                                      .model(model)
                                      .messages(messages)
                                      .temperature(0.7f)
                                      .build();

    }
    
}
/*
 * {
  "model": "gpt-4-turbo",
  "messages": [
    {
      "role": "system",
      "content": "You are a helpful assistant."
    },
    {
      "role": "user",
      "content": "What is the capital of France?"
    }
  ],
  "temperature": 0.7,
  "max_tokens": 100,
  "top_p": 1,
  "frequency_penalty": 0,
  "presence_penalty": 0
}

 */
