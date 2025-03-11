package org.scit.project.mypage.repository;

import java.util.List;

import org.scit.project.mypage.dto.MyReplyDTO;
import org.scit.project.reply.entity.ReplyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MyReplyRepository extends JpaRepository<ReplyEntity, Long> {

	@Query("SELECT new org.scit.project.mypage.dto.MyReplyDTO(b.boardSeq, b.boardTitle, r.replyContent, r.createDate) " +
		       "FROM ReplyEntity r JOIN r.board b " +
		       "WHERE r.user.userSeq = :userSeq " +
		       "ORDER BY r.createDate DESC")
		List<MyReplyDTO> findByUserSeqOrderByCreateDateDesc(@Param("userSeq") Long userSeq);


}
