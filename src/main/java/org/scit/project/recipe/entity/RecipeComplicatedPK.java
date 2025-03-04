package org.scit.project.recipe.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class RecipeComplicatedPK implements Serializable {

    @Column(name = "recipe_seq")
    private Long recipeSeq;

    @Column(name = "keyword")
    private String keyword;
    
    public static RecipeComplicatedPK of(Long recipeSeq, String keyword) {
        return RecipeComplicatedPK.builder()
                                    .recipeSeq(recipeSeq)
                                    .keyword(keyword)
                                    .build();
    }
    
}
