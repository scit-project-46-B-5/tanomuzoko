package org.scit.project.recipe.dto;

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
@NoArgsConstructor
public class RecipeConditionDTO {
    private String usage;
    private String menu;
    private String taste;
    private String level;

    public static RecipeConditionDTO TODTO(String usage, String menu, String taste, String level) {
        return RecipeConditionDTO.builder().usage(usage)
                                            .menu(menu)
                                            .taste(taste)
                                            .level(level)
                                            .build();
    }
}
