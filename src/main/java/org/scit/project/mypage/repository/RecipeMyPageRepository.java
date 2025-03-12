package org.scit.project.mypage.repository;

import org.scit.project.recipe.entity.RecipeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface RecipeMyPageRepository extends JpaRepository<RecipeEntity, Long>{
    
    @EntityGraph(attributePaths = {"recipeOutputEntity", "recipeInputKeywordEntityList"})
    @Query("""
        SELECT DISTINCT r FROM RecipeEntity r
        INNER JOIN r.recipeInputKeywordEntityList k
        INNER JOIN r.recipeOutputEntity j
        INNER JOIN r.userEntity u
        WHERE u.userSeq = :userSeq
    """)
    Page<RecipeEntity> findRecipesWithPagination(
        @Param("userSeq") Long userSeq,
        Pageable pageable
    );
    
} 
