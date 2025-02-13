package org.scit.project.board.service;

import org.scit.project.board.dto.BoardDTO;
import org.scit.project.board.entity.BoardEntity;
import org.scit.project.board.repository.BoardRepository;
import org.scit.project.board.util.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {
	private final BoardRepository boardRepository;

	@Value("${spring.servlet.multipart.location}")
	private String uploadPath;
	
	public void insertBoard(BoardDTO boardDTO) {
		MultipartFile uploadFile = boardDTO.getUploadFile();
		String savedFileName = null;
		String originalFileName = null;
		
		if(!uploadFile.isEmpty() ) {
			savedFileName = FileService.saveFile(boardDTO.getUploadFile(), uploadPath);
			originalFileName = uploadFile.getOriginalFilename();
		}
			
		boardDTO.setSavedFileName(savedFileName);
		boardDTO.setOriginalFileName(originalFileName);
		
	    // boardSeq가 null인지 확인 후 기본값 설정
	    if (boardDTO.getBoardSeq() == null) {
	        boardDTO.setBoardSeq(0L); // 또는 DB에서 자동 증가하도록 null 유지
	    }
		
		BoardEntity entity = BoardEntity.toEntity(boardDTO);
		
		boardRepository.save(entity);
	}
}
