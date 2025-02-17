package org.scit.project.mainpage.repository;

import org.scit.project.board.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MainRepository extends JpaRepository<BoardEntity, Long> {

}
