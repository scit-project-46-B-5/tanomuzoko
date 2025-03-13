package org.scit.project.mypage.repository;

import org.scit.project.mypage.dto.MyReplyDTO;
import org.scit.project.reply.entity.ReplyEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MyReplyRepository extends JpaRepository<ReplyEntity, Long> {

	@Query("SELECT new org.scit.project.mypage.dto.MyReplyDTO(b.boardSeq, b.boardTitle, r.replyContent, r.createDate) " +
		       "FROM ReplyEntity r JOIN r.board b " +
		       "WHERE r.user.userSeq = :userSeq ")
	Page<MyReplyDTO> findByUserSeqOrderByCreateDateDesc(@Param("userSeq") Long userSeq, Pageable pageable);


}
