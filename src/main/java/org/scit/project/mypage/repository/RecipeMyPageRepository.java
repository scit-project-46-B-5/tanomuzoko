package org.scit.project.mypage.repository;

import java.util.List;

import org.scit.project.mypage.dto.RecipeWrittenDTO;
import org.scit.project.recipe.entity.RecipeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface RecipeMyPageRepository extends JpaRepository<RecipeEntity, Long>{
    
    // @EntityGraph(attributePaths = {"recipeOutputEntity", "recipeInputKeywordEntityList"})
    // @Query("""
    //     SELECT DISTINCT r FROM RecipeEntity r
    //     INNER JOIN r.recipeInputKeywordEntityList k
    //     INNER JOIN r.recipeOutputEntity j
    //     INNER JOIN r.userEntity u
    //     WHERE u.userSeq = :userSeq
    // """)
    // Page<RecipeEntity> findRecipesWithPagination(
    //     @Param("userSeq") Long userSeq,
    //     Pageable pageable
    // );
    
    @Query("""
        SELECT r.recipeSeq FROM RecipeEntity r
        WHERE r.userEntity.userSeq = :userSeq
    """)
    List<Long> findRecipeIdsByUser(@Param("userSeq") Long userSeq);

    @EntityGraph(attributePaths = {"recipeOutputEntity", "recipeInputKeywordEntityList"})
    @Query(value = """
        SELECT r FROM RecipeEntity r
        INNER JOIN r.recipeInputKeywordEntityList k
        INNER JOIN r.recipeOutputEntity j
        WHERE r.recipeSeq IN :recipeIds
        and r.isDeleted = false
    """, countQuery = """
            select count(r) FROM RecipeEntity r
            WHERE r.recipeSeq IN :recipeIds
            and r.isDeleted = false
    """)
    Page<RecipeEntity> findRecipesByIds(@Param("recipeIds") List<Long> recipeIds, Pageable pageable);


    @Query("""
        SELECT b.boardSeq as boardSeq, r.recipeSeq as recipeSeq
        from BoardEntity b
        INNER JOIN b.recipeEntity r
        WHERE b.isDeleted = false
        AND b.userEntity.userSeq = :userSeq
    """)
    List<RecipeWrittenDTO> findRecipeAndBoardWritten(@Param("userSeq") Long userSeq);

} 
