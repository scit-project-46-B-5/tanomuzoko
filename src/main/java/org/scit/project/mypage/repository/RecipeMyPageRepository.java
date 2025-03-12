package org.scit.project.mypage.repository;

import java.util.List;

import org.scit.project.recipe.entity.RecipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeMyPageRepository extends JpaRepository<RecipeEntity, Long>{

    List<RecipeEntity> findByUserEntity_UserSeqAndRecipeInputKeywordEntityListIsNotNullAndRecipeOutputEntityIsNotNull(Long userSeq);
    
} 
