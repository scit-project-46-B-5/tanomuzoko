package org.scit.project.mypage.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.scit.project.recipe.entity.RecipeEntity;

import lombok.Builder;

@Builder
public record RecipeMyPageResponse(
                                    Long id,
                                    String title,
                                    String outputHTML,
                                    List<String> inputKeywords,
                                    LocalDateTime createdDateTime
                                ) {

    public static RecipeMyPageResponse toDTO(RecipeEntity recipeEntity) {
		return RecipeMyPageResponse.builder()
				.id(recipeEntity.getRecipeSeq())
                .title(recipeEntity.getRecipeOutputEntity().getRecipeTitle())
                .outputHTML(recipeEntity.getRecipeOutputEntity().getOutputContent())
                .inputKeywords(recipeEntity.getRecipeInputKeywordEntityList().stream().map((entity) -> entity.getRecipeComplicatedPK().getKeyword()).toList())
                .createdDateTime(recipeEntity.getCreatedAt())
				.build();
	}
    
}
