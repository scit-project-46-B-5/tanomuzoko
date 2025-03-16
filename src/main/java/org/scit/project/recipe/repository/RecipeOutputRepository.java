package org.scit.project.recipe.repository;

import java.util.Optional;

import org.scit.project.recipe.entity.RecipeEntity;
import org.scit.project.recipe.entity.RecipeOutputEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeOutputRepository extends JpaRepository<RecipeOutputEntity, Long> {

    Optional<RecipeOutputEntity> findByRecipeEntity(RecipeEntity recipeEntity);
} 