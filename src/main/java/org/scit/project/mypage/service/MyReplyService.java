package org.scit.project.mypage.service;

import org.scit.project.mypage.dto.MyReplyDTO;
import org.scit.project.mypage.repository.MyReplyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MyReplyService {
	
	private final MyReplyRepository myReplyRepository;

	public  Page<MyReplyDTO> getMyReplies(Long userSeq, int page, int size) {
		 Pageable pageable = PageRequest.of(page, size, Sort.by("createDate").descending());
		 
		 return myReplyRepository.findByUserSeqOrderByCreateDateDesc(userSeq, pageable);
		 
	}
}
