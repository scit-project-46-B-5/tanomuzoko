package org.scit.project.board.service;

import java.util.Optional;
import java.util.UUID;
import org.scit.project.board.dto.BoardDTO;
import org.scit.project.board.entity.BoardEntity;
import org.scit.project.board.entity.BoardImageEntity;
import org.scit.project.board.repository.BoardRepository;
import org.scit.project.board.repository.BoardImageRepository;
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
    private final BoardImageRepository boardImageRepository;

    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    public void insertBoard(BoardDTO boardDTO) {
        LoginUserDetails loginUser = (LoginUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = loginUser.getUserId();
        UserEntity user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("no such user"));

        BoardEntity entity = BoardEntity.toEntity(boardDTO, user);
        BoardEntity savedBoard = boardRepository.save(entity);

        // 썸네일 값이 존재하면 board_image 테이블에 저장
        if (boardDTO.getThumbnail() != null && !boardDTO.getThumbnail().isEmpty()) {
            String fileUrl = boardDTO.getThumbnail();
            // 파일 URL이 "/uploads/filename" 형태라면 파일명만 추출
            String originalFileName = fileUrl.startsWith("/uploads/") ? fileUrl.substring(9) : fileUrl;
            
            // UUID 형식의 파일명 생성 (원본 파일명에 확장자가 있다면 그대로 사용)
            String uuidFileName = UUID.randomUUID().toString();
            if (originalFileName.contains(".")) {
                String ext = originalFileName.substring(originalFileName.lastIndexOf("."));
                uuidFileName += ext;
            }
            
            BoardImageEntity imageEntity = BoardImageEntity.builder()
                .boardEntity(savedBoard)
                .originalFileName(originalFileName)  // 원본 파일명은 그대로 (base64 형식)
                .savedFileName(uuidFileName)           // UUID 형식의 파일명 저장
                .build();
            boardImageRepository.save(imageEntity);
        }
    }

    public BoardDTO selectOne(Long boardSeq) {
        Optional<BoardEntity> temp = boardRepository.findById(boardSeq);
        if (!temp.isPresent()) {
            return null;
        } else {
            return BoardDTO.toDTO(temp.get());
        }
    }
}
