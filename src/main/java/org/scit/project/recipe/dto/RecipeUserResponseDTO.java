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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Setter
@Getter
public class RecipeUserResponseDTO {
    String title;
    String[] ingredients;
    String[] cookingMethod;

    public static RecipeUserResponseDTO TODTO(String title, String[] ingredients, String[] cookingMethods) {
        return RecipeUserResponseDTO.builder()
                                    .title(title)
                                    .ingredients(ingredients)
                                    .cookingMethod(cookingMethods)
                                    .build();
    }
}