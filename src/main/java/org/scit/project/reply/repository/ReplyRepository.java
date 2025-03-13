package org.scit.project.reply.repository;

import java.util.List;

import org.scit.project.board.entity.BoardEntity;
import org.scit.project.reply.entity.ReplyEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReplyRepository extends JpaRepository<ReplyEntity, Long>{

    List<ReplyEntity> findByParentReplyAndIsDeletedFalse(ReplyEntity parentReply);

    List<ReplyEntity> findByBoardAndParentReplyIsNullAndIsDeletedFalse(BoardEntity board, Sort sort);

    @Query("""
        SELECT DISTINCT r FROM ReplyEntity r 
        LEFT JOIN FETCH r.childReplies c
        WHERE r.board = :board 
        AND (r.parentReply IS NULL OR r.parentReply IN 
        (SELECT p FROM ReplyEntity p WHERE p.board = :board AND p.parentReply IS NULL))
        ORDER BY COALESCE(r.parentReply.replySeq, r.replySeq)
    """)
    Page<ReplyEntity> findRepliesByBoard(@Param("board") BoardEntity board, Pageable pageable);
}
