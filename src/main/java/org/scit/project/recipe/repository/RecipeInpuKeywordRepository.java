package org.scit.project.recipe.repository;

import org.scit.project.recipe.entity.RecipeComplicatedPK;
import org.scit.project.recipe.entity.RecipeInputKeywordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeInpuKeywordRepository extends JpaRepository<RecipeInputKeywordEntity, RecipeComplicatedPK> {

    
} 