package org.scit.project.recipe.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "recipe_input_keyword")
public class RecipeInputKeywordEntity {

    @EmbeddedId
    private RecipeComplicatedPK recipeComplicatedPK;

    @MapsId("recipeSeq")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_seq", referencedColumnName = "recipe_seq")
    private RecipeEntity recipeEntity;

    public static RecipeInputKeywordEntity TOENTITY(RecipeEntity recipeEntity, String keyword) {
        return RecipeInputKeywordEntity.builder().recipeComplicatedPK(RecipeComplicatedPK.of(recipeEntity.getRecipeSeq(), keyword))
                                                    .recipeEntity(recipeEntity)
                                                    .build();
    }

}
