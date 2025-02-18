package org.scit.project.board.service;

import org.scit.project.board.dto.BoardDTO;
import org.scit.project.board.entity.BoardEntity;
import org.scit.project.board.repository.BoardRepository;
import org.scit.project.user.dto.LoginUserDetails;
import org.scit.project.user.entity.UserEntity;
import org.scit.project.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {
	private final BoardRepository boardRepository;
	private final UserRepository userRepository;
	

	@Value("${spring.servlet.multipart.location}")
	private String uploadPath;
	
	public void insertBoard(BoardDTO boardDTO) {
		LoginUserDetails loginUser =  (LoginUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String userId = loginUser.getUserId();
		UserEntity user = userRepository.findByUserId(userId).orElseThrow(()->new RuntimeException("no such user"));
		
	    BoardEntity entity = BoardEntity.toEntity(boardDTO, user);
	    boardRepository.save(entity);
	}
}
