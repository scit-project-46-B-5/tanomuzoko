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
	    
	    if (uploadFile != null && !uploadFile.isEmpty()) {
	        String savedFileName = FileService.saveFile(uploadFile, uploadPath);
	        boardDTO.setSavedFileName(savedFileName);
	        boardDTO.setOriginalFileName(uploadFile.getOriginalFilename());
	    }
	    
	    BoardEntity entity = BoardEntity.toEntity(boardDTO);
	    boardRepository.save(entity);
	}
}
