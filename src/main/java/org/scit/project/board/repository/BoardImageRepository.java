package org.scit.project.board.repository;

import org.scit.project.board.entity.BoardImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardImageRepository extends JpaRepository<BoardImageEntity, Long> {
}
