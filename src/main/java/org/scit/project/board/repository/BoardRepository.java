package org.scit.project.board.repository;

import java.util.List;

import org.scit.project.board.entity.BoardEntity;
import org.scit.project.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {

    // 해당 작성자의 최신 게시물 10개를 생성일 기준 내림차순 정렬로 조회
    List<BoardEntity> findTop11ByUserEntityOrderByCreateDateDesc(UserEntity userEntity);

    // 조회수(hitCount)만 증가시키는 update 쿼리 (update_date에는 영향을 주지 않음)
    @Modifying
    @Query("update BoardEntity b set b.hitCount = b.hitCount + 1 where b.boardSeq = :boardSeq")
    void incrementHitCount(@Param("boardSeq") Long boardSeq);
    
    // 현재 로그인한 사용자의 레시피 목록 조회 (레시피 id와 제목)
    // recipe와 recipe_output_content 테이블을 조인하고, board 테이블에 해당 recipe가 등록되지 않은 레시피만 조회
    @Query(value = "SELECT r.recipe_seq, roc.recipe_title FROM recipe r " +
                   "JOIN recipe_output_content roc ON r.recipe_seq = roc.recipe_seq " +
                   "WHERE r.user_seq = :userSeq " +
                   "AND NOT EXISTS (SELECT 1 FROM board b WHERE b.recipe_seq = r.recipe_seq) " +
                   "ORDER BY r.recipe_seq DESC", nativeQuery = true)
    List<Object[]> findRecipesByUser(@Param("userSeq") Long userSeq);
}
