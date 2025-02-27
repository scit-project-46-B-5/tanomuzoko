package org.scit.project.board.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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

import jakarta.transaction.Transactional;
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
        // thumbnailUrl: 업로드된 파일의 URL (예: "/uploads/강아지1_UUID.jpg")
        // thumbnail: 에디터에 삽입된 base64 데이터 (원본_file_name으로 저장)
        if (boardDTO.getThumbnailUrl() != null && !boardDTO.getThumbnailUrl().isEmpty()) {
            String thumbnailUrl = boardDTO.getThumbnailUrl();
            String savedFileName = "";
            // URL 형태라면 FileService에서 생성한 파일명을 그대로 사용
            if (thumbnailUrl.startsWith("/uploads/")) {
                savedFileName = thumbnailUrl.substring(9); // "/uploads/" 제거
            } else {
                // 혹시 base64 형식이면 (예외 상황)
                String newUUID = UUID.randomUUID().toString();
                String ext = "";
                if (thumbnailUrl.startsWith("data:image/")) {
                    int slashIndex = thumbnailUrl.indexOf("/");
                    int semicolonIndex = thumbnailUrl.indexOf(";");
                    if (slashIndex != -1 && semicolonIndex != -1) {
                        ext = "." + thumbnailUrl.substring(slashIndex + 1, semicolonIndex);
                    }
                }
                savedFileName = "thumbnail_" + newUUID + ext;
            }
            
            BoardImageEntity imageEntity = BoardImageEntity.builder()
                .boardEntity(savedBoard)
                .originalFileName(boardDTO.getThumbnail())  // base64 데이터를 그대로 저장
                .savedFileName(savedFileName)                // "원본파일이름_UUID.확장자" 형식
                .build();
            boardImageRepository.save(imageEntity);
        }
    }

    @Transactional
    public BoardDTO selectOne(Long boardSeq) {
        // 조회수(hitCount)만 증가시키는 커스텀 쿼리 사용 → update_date에는 영향 없음
        boardRepository.incrementHitCount(boardSeq);
        BoardEntity boardEntity = boardRepository.findById(boardSeq).orElse(null);
        if (boardEntity == null) {
            return null;
        }
        return BoardDTO.toDTO(boardEntity);
    }
    
    @Transactional
    public List<BoardDTO> getRecentPostsByUser(Long userSeq) {
        // 해당 userSeq에 해당하는 UserEntity 조회
        UserEntity user = userRepository.findById(userSeq)
                .orElseThrow(() -> new RuntimeException("User not found"));
        // 최신 게시물 10개 조회 (작성일 내림차순)
        List<BoardEntity> boardEntities = boardRepository.findTop10ByUserEntityOrderByCreateDateDesc(user);
        return boardEntities.stream()
                .map(BoardDTO::toDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public List<BoardDTO> getPopularPosts() {
        // 인기 게시글 5개 조회 (hitCount 기준 내림차순)
        List<BoardEntity> popularEntities = boardRepository.findTop5ByOrderByHitCountDesc();
        return popularEntities.stream()
                .map(BoardDTO::toDTO)
                .collect(Collectors.toList());
    }
}
