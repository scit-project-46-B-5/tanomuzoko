package org.scit.project.recipe.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
@Table(name = "recipe_output_content")
public class RecipeOutputEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_output_content_seq")
    private Long recipeOutputContentSeq;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_seq", referencedColumnName = "recipe_seq")
    private RecipeEntity recipeEntity;

    @Column(name = "recipe_title")
    private String recipeTitle;

    @Column(name = "output_content")
    private String outputContent;

    public static RecipeOutputEntity TOENTTIY(RecipeEntity recipeEntity, String recipeTitle, String outputContent) {
        return RecipeOutputEntity.builder()
                                    .recipeEntity(recipeEntity)
                                    .recipeTitle(recipeTitle)
                                    .outputContent(outputContent)
                                    .build();
    }

    
}
