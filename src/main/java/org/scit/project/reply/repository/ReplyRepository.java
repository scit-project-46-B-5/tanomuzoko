package org.scit.project.reply.repository;

import org.scit.project.board.entity.BoardEntity;
import org.scit.project.reply.entity.ReplyEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<ReplyEntity, Long>{

    Page<ReplyEntity> findAllByBoardAndIsDeletedFalse(BoardEntity board, Pageable pageable);
    
}
