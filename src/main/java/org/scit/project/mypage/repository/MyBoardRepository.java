package org.scit.project.mypage.repository;

import java.util.List;

import org.scit.project.mypage.dto.MyBoardDto;
import org.scit.project.mypage.entity.MyBoardEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MyBoardRepository extends JpaRepository<MyBoardEntity, Long> {

	@Query("SELECT new org.scit.project.mypage.dto.MyBoardDto(b.boardSeq, b.boardTitle, b.boardContent, COALESCE(i.originalFileName, ''), COALESCE(COUNT(h), 0)) " +
		       "FROM MyBoardEntity b " +
		       "LEFT JOIN b.images i " +
		       "LEFT JOIN b.hearts h " + 
		       "WHERE b.userSeq = :userSeq " +  
		       "GROUP BY b.boardSeq, COALESCE(i.originalFileName, '')")
		List<MyBoardDto> findMyBoards(@Param("userSeq") Long userSeq, Pageable pageable);
}
