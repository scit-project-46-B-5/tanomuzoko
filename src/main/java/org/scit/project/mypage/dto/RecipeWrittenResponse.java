package org.scit.project.mypage.dto;

import lombok.Builder;

@Builder
public record RecipeWrittenResponse(Long boardSeq, Long recipeSeq) {
    public static RecipeWrittenResponse TODTO(RecipeWrittenDTO recipeWrittenDTO) {
        return RecipeWrittenResponse.builder()
                                    .boardSeq(recipeWrittenDTO.getBoardSeq())
                                    .recipeSeq(recipeWrittenDTO.getRecipeSeq())
                                    .build();
    }
    
}
