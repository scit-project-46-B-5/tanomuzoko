package org.scit.project.mypage.repository;

import org.scit.project.mypage.dto.MyBoardDto;
import org.scit.project.mypage.entity.MyBoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MyBoardRepository extends JpaRepository<MyBoardEntity, Long> {

    // 사용자가 작성한 게시물 조회

	@Query("SELECT new org.scit.project.mypage.dto.MyBoardDto(b.boardSeq, b.boardTitle, b.boardContent, " +
		       "COALESCE(i.originalFileName, ''), COALESCE(COUNT(h), 0)) " +
		       "FROM MyBoardEntity b " +
		       "LEFT JOIN b.images i " +
		       "LEFT JOIN MyBoardHeartEntity h ON h.myBoardEntity.boardSeq = b.boardSeq AND h.isHearted = TRUE " +
		       "WHERE b.userSeq = :userSeq AND b.isDeleted = false " +
		       "GROUP BY b.boardSeq, b.boardTitle, b.boardContent, i.originalFileName " +
		       "ORDER BY b.createDate DESC")
		Page<MyBoardDto> findMyBoards(@Param("userSeq") Long userSeq, Pageable pageable);

    // 사용자가 좋아요를 누른 게시물 조회
    @Query("SELECT new org.scit.project.mypage.dto.MyBoardDto(b.boardSeq, b.boardTitle, b.boardContent, COALESCE(i.originalFileName, ''), COALESCE(COUNT(h), 0)) " +
           "FROM MyBoardEntity b " +
           "LEFT JOIN b.images i " +
           "LEFT JOIN b.hearts h " + 
           "WHERE h.userEntity.userSeq = :userSeq AND h.isHearted = TRUE AND b.isDeleted = false " +
           "GROUP BY b.boardSeq, COALESCE(i.originalFileName, '') " +
           "ORDER BY b.createDate DESC")
    Page<MyBoardDto> findBoardsWithLikes(@Param("userSeq") Long userSeq, Pageable pageable);
}
