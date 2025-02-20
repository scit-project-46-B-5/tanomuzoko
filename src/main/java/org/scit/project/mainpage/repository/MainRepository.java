package org.scit.project.mainpage.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.scit.project.board.entity.BoardEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MainRepository extends JpaRepository<BoardEntity, Long> {

    @Query("SELECT b FROM BoardEntity b WHERE b.createDate >= :startDate ORDER BY (SELECT COUNT(h) FROM BoardHeartEntity h WHERE h.board = b AND h.isHearted = true) DESC")
    List<BoardEntity> findTopPostsByHeartCount(@Param("startDate") LocalDateTime startDate, Pageable pageable);
}
