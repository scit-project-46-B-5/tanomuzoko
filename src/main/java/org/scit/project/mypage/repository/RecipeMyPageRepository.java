package org.scit.project.mypage.repository;

import org.scit.project.recipe.entity.RecipeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface RecipeMyPageRepository extends JpaRepository<RecipeEntity, Long>{
    
    @Query("""
        SELECT DISTINCT r FROM RecipeEntity r
        JOIN r.recipeInputKeywordEntityList k
        WHERE r.userEntity.userSeq = :userSeq
        AND r.recipeOutputEntity IS NOT NULL
    """)
    Page<RecipeEntity> findRecipesWithPagination(
        @Param("userSeq") Long userSeq,
        Pageable pageable
    );
    
} 
