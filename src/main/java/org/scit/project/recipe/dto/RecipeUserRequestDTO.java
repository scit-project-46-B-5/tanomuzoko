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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Setter
@Getter
public class RecipeUserRequestDTO {
    String ingredients;
    String option1;
    String option2;
    String option3;
    String option4;
    String option5;
}
