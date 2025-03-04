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
    List<BoardEntity> findTop10ByUserEntityOrderByCreateDateDesc(UserEntity userEntity);

    // 조회수(hitCount)만 증가시키는 update 쿼리 (update_date에는 영향을 주지 않음)
    @Modifying
    @Query("update BoardEntity b set b.hitCount = b.hitCount + 1 where b.boardSeq = :boardSeq")
    void incrementHitCount(@Param("boardSeq") Long boardSeq);
    
    // 인기 게시글 5개를 조회 (hitCount 기준 내림차순)
    List<BoardEntity> findTop5ByOrderByHitCountDesc();
}
