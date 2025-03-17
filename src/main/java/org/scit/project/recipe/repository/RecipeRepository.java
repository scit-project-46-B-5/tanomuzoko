package org.scit.project.recipe.repository;

import java.util.List;

import org.scit.project.recipe.dto.RecipeProjection;
import org.scit.project.recipe.entity.RecipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RecipeRepository extends JpaRepository<RecipeEntity, Long> {

    @Query("""
             SELECT r.recipeSeq AS id, roc.recipeTitle AS title FROM RecipeEntity r
             JOIN r.recipeOutputEntity roc
             WHERE r.userEntity.userSeq = :userSeq
             AND NOT EXISTS (SELECT 1 FROM r.boardEntity b where b.isDeleted = false)
             ORDER BY r.recipeSeq DESC
            """)
    List<RecipeProjection> findRecipesByUser(@Param("userSeq") Long userSeq);

}
