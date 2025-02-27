package org.scit.project.mainpage.repository;

import org.scit.project.board.entity.BoardEntity;
import org.scit.project.mainpage.dto.BoardWithHeartCountDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MainRepository extends JpaRepository<BoardEntity, Long> {

    @Query("""
        SELECT b AS board, 
               (SELECT COUNT(h) FROM BoardHeartEntity h WHERE h.board = b AND h.isHearted = true) AS heartCount
        FROM BoardEntity b
        ORDER BY b.createDate DESC
    """)
    Page<BoardWithHeartCountDTO> findAllWithHeartCount(Pageable pageable);
}
