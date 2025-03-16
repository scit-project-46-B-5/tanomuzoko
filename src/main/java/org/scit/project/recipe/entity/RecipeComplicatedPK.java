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

/**
 * compound PK 구성을 위한 class
 * recipe_seq와 keyword로 복합키가 구성된다.
 * compound PK로서 RecipeInputKeywordEntity에서 사용한다.
 */
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
