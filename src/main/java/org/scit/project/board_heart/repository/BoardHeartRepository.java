package org.scit.project.board_heart.repository;

import java.util.Optional;

import org.scit.project.board.entity.BoardEntity;
import org.scit.project.board_heart.entity.BoardHeartEntity;
import org.scit.project.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardHeartRepository extends JpaRepository<BoardHeartEntity, Long> {
    Optional<BoardHeartEntity> findByBoardAndUser(BoardEntity board, UserEntity user);

    int countByBoardAndIsHeartedTrue(BoardEntity board); // 공감 개수 조회
}
