package org.scit.project.recipe.dto;

import java.util.ArrayList;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Setter
@Getter
public class RecipeUserResponseDTO  {
    private String title;
    private String[] ingredients;
    private String[] cookingMethods;
    private RecipeConditionDTO recipeConditionDTO;

    public static RecipeUserResponseDTO TODTO(String title, String[] ingredients, String[] cookingMethods, RecipeConditionDTO recipeConditionDTO) {
        return RecipeUserResponseDTO.builder()
                                    .title(title)
                                    .ingredients(ingredients)
                                    .cookingMethods(cookingMethods)
                                    .recipeConditionDTO(recipeConditionDTO)
                                    .build();
    }
}