package org.scit.project.board_heart.repository;

import java.util.List;
import java.util.Optional;

import org.scit.project.board.entity.BoardEntity;
import org.scit.project.board_heart.entity.BoardHeartEntity;
import org.scit.project.mainpage.dto.BoardWithHeartCountDTO;
import org.scit.project.user.entity.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BoardHeartRepository extends JpaRepository<BoardHeartEntity, Long> {
    Optional<BoardHeartEntity> findByBoardAndUser(BoardEntity board, UserEntity user);

    int countByBoardAndIsHeartedTrue(BoardEntity board); // 공감 개수 조회

    @Query("""
                SELECT b AS board, COUNT(h) AS heartCount
                FROM BoardHeartEntity h
                JOIN h.board b
                WHERE h.isHearted = true
                GROUP BY b
                ORDER BY heartCount DESC
            """)
    List<BoardWithHeartCountDTO> findTop3LikedBoards(Pageable pageable);
}
