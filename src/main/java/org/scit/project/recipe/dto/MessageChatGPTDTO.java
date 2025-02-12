package org.scit.project.recipe.dto;

import java.io.Serializable;

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
public class MessageChatGPTDTO implements Serializable {

    private String role;
    private String content;

}
