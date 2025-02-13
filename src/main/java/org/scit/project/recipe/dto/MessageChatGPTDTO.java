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


    public static MessageChatGPTDTO TOSYSTEMMESSAGE(RecipeUserRequestDTO recipeUserRequestDTO) {
        return MessageChatGPTDTO.builder()
                                    .role("system")
                                    .content("너는 이제 음식 레시피를 추천해주는거야")
                                    .build();

    }

    public static MessageChatGPTDTO TOUSERMESSAGE(RecipeUserRequestDTO recipeUserRequestDTO) {
        //TODO
        //recipeUserRequestDTO

        String content="""
                재료: 항정살, 양파, 배추
                맛: 짠맛
                음식종류: 일식
                난이도: 쉬움
                100자 이내로 작성해줘. 출력은 다음과 같이 양식을 맞춰서 출력해줘
                조리방법은 5개 step으로만 구성해줘.
                --------------------
                타이틀: 요리이름
                재료: 재료1, 재료2, 재료3, 재료4, 재료5
                조리방법: 1. ~~합니다
                         2. ~~합니다
                """;

        return MessageChatGPTDTO.builder()
                                    .role("user")
                                    .content(content)
                                    .build();

    }

}
