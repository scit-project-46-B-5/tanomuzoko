//package org.scit.project.mypage.service;
//
//import org.scit.project.mypage.dto.MyBoardDto;
//import org.scit.project.mypage.repository.MyBoardRepository;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.stereotype.Service;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class MypageService {
//	
//	private final MyBoardRepository boardRepository;
//	
//	
//	public Page<MyBoardDto> getMyBoards(Long userSeq, int page, int size) {
//		Pageable pageable = PageRequest.of(page, size, Sort.by("createDate").descending());
//		
//	    return boardRepository.findMyBoards(userSeq, pageable);
//	}
//
//	public Page<MyBoardDto> getLikedBoards(Long userSeq, int page, int size) {
//		Pageable pageable = PageRequest.of(page, size, Sort.by("createDate").descending()); 
//		
//		return boardRepository.findBoardsWithLikes(userSeq, pageable);
//	}
//
//
//}
