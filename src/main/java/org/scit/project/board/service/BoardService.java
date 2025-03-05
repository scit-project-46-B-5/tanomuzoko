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
import org.scit.project.board_heart.repository.BoardHeartRepository;
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
    // BoardHeartRepository를 주입받아 공감수 기준 정렬에 사용
    private final BoardHeartRepository boardHeartRepository;

    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    public void insertBoard(BoardDTO boardDTO) {
        LoginUserDetails loginUser = (LoginUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = loginUser.getUserId();
        UserEntity user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("no such user"));

        BoardEntity entity = BoardEntity.toEntity(boardDTO, user);
        BoardEntity savedBoard = boardRepository.save(entity);

        if (boardDTO.getThumbnailUrl() != null && !boardDTO.getThumbnailUrl().isEmpty()) {
            String thumbnailUrl = boardDTO.getThumbnailUrl();
            String savedFileName = "";

            if (thumbnailUrl.startsWith("/uploads/")) {
                savedFileName = thumbnailUrl.substring(9);
            } else {
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
                .originalFileName(boardDTO.getThumbnail())
                .savedFileName(savedFileName)
                .build();
            boardImageRepository.save(imageEntity);
        }
    }

    @Transactional
    public BoardDTO selectOne(Long boardSeq) {
        boardRepository.incrementHitCount(boardSeq);
        BoardEntity boardEntity = boardRepository.findById(boardSeq).orElse(null);
        if (boardEntity == null) {
            return null;
        }
        return BoardDTO.toDTO(boardEntity);
    }

    @Transactional
    public List<BoardDTO> getRecentPostsByUser(Long userSeq, Long currentBoardSeq) {
        UserEntity user = userRepository.findById(userSeq)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<BoardEntity> boardEntities = boardRepository.findTop10ByUserEntityOrderByCreateDateDesc(user)
                .stream()
                .filter(board -> !board.getBoardSeq().equals(currentBoardSeq)) // 현재 게시글 제외
                .limit(10)
                .collect(Collectors.toList());

        return boardEntities.stream()
                .map(BoardDTO::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<BoardDTO> getPopularPosts() {
        // 모든 게시글을 가져와 각 게시글의 공감 수를 기준으로 내림차순 정렬한 후 상위 5개 반환
        List<BoardEntity> allBoards = boardRepository.findAll();
        List<BoardDTO> popularPosts = allBoards.stream()
            .sorted((b1, b2) -> {
                int heartCount1 = boardHeartRepository.countByBoardAndIsHeartedTrue(b1);
                int heartCount2 = boardHeartRepository.countByBoardAndIsHeartedTrue(b2);
                return Integer.compare(heartCount2, heartCount1);
            })
            .limit(5)
            .map(BoardDTO::toDTO)
            .collect(Collectors.toList());
        return popularPosts;
    }

	public BoardDTO updateSelectOne(Long boardSeq) {
		Optional<BoardEntity> temp = boardRepository.findById(boardSeq);

		if(!temp.isPresent()) return null;

		return BoardDTO.toDTO(temp.get());
	}

    @Transactional
    public void updateBoard(BoardDTO boardDTO) {
        BoardEntity entity = boardRepository.findById(boardDTO.getBoardSeq())
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));

        entity.setBoardTitle(boardDTO.getBoardTitle());
        entity.setBoardContent(boardDTO.getBoardContent());
        boardRepository.save(entity);
    }
}
