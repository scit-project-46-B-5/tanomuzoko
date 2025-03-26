package org.scit.project.mainpage.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.scit.project.board.entity.BoardEntity;
import org.scit.project.mainpage.dto.BoardWithHeartCountDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MainRepository extends JpaRepository<BoardEntity, Long> {

    @Query("""
        SELECT b AS board, 
               (SELECT COUNT(h) FROM BoardHeartEntity h WHERE h.board = b AND h.isHearted = true) AS heartCount
        FROM BoardEntity b
        WHERE b.isDeleted = false
        AND (:search IS NULL OR b.boardTitle LIKE %:search%)
        ORDER BY b.createDate DESC
    """)
    Page<BoardWithHeartCountDTO> findAllWithHeartCountAndIsDeletedIsFalseContainingBoardTitle(@Param("search") String search, Pageable pageable);

    @Query("""
        SELECT b AS board, 
               (SELECT COUNT(h) FROM BoardHeartEntity h WHERE h.board = b AND h.isHearted = true) AS heartCount
        FROM BoardEntity b
        WHERE b.createDate >= :startDate
        AND b.isDeleted = false
        ORDER BY heartCount DESC
    """)
    List<BoardWithHeartCountDTO> findTopPostsByPeriodAndHeartCountAndIsDeletedIsFalse(
            @Param("startDate") LocalDateTime startDate, Pageable pageable);


}
