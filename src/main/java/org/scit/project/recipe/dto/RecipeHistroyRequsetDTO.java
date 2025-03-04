package org.scit.project.recipe.dto;

import java.util.Arrays;
import java.util.List;

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
public class RecipeHistroyRequsetDTO {
    private String title;
    private String outputContent;
    private RecipeConditionDTO recipeCondition;

    public List<String> getAllConditions() {
        return Arrays.asList(recipeCondition.getUsage(), 
                                recipeCondition.getMenu(), 
                                recipeCondition.getTaste(), 
                                recipeCondition.getLevel()
                            );
    }

}
