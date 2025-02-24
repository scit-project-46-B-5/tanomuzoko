package org.scit.project.board.repository;

import java.util.Optional;
import org.scit.project.board.entity.BoardEntity;
import org.scit.project.board.entity.BoardImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardImageRepository extends JpaRepository<BoardImageEntity, Long> {
    Optional<BoardImageEntity> findByBoardEntity(BoardEntity boardEntity);
}
