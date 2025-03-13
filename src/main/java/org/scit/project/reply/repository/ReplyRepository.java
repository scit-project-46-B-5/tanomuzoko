package org.scit.project.reply.repository;

import java.util.List;

import org.scit.project.board.entity.BoardEntity;
import org.scit.project.reply.entity.ReplyEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<ReplyEntity, Long>{

    List<ReplyEntity> findByParentReplyAndIsDeletedFalse(ReplyEntity parentReply);

    List<ReplyEntity> findByBoardAndParentReplyIsNullAndIsDeletedFalse(BoardEntity board, Sort sort);
}
