package org.scit.project.board_heart.service;

import java.util.Optional;
import org.scit.project.board.entity.BoardEntity;
import org.scit.project.board.repository.BoardRepository;
import org.scit.project.board_heart.dto.BoardHeartResponseDTO;
import org.scit.project.board_heart.entity.BoardHeartEntity;
import org.scit.project.board_heart.repository.BoardHeartRepository;
import org.scit.project.user.entity.UserEntity;
import org.scit.project.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardHeartService {

    private final BoardHeartRepository boardHeartRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    public BoardHeartResponseDTO toggleHeart(Long boardSeq, Long userId) {
        BoardEntity board = boardRepository.findById(boardSeq)
                .orElseThrow(() -> new RuntimeException("해당 게시글을 찾을 수 없습니다."));
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 사용자를 찾을 수 없습니다."));

        Optional<BoardHeartEntity> heartEntityOptional = boardHeartRepository.findByBoardAndUser(board, user);

        boolean isHearted;
        if (heartEntityOptional.isPresent()) {
            BoardHeartEntity heartEntity = heartEntityOptional.get();
            heartEntity.setIsHearted(!heartEntity.getIsHearted()); // 공감 상태 반전
            isHearted = heartEntity.getIsHearted();
        } else {
            BoardHeartEntity heartEntity = BoardHeartEntity.toEntity(board, user, true); // ✅ `toEntity` 사용
            boardHeartRepository.save(heartEntity);
            isHearted = true;
        }

        int heartCount = boardHeartRepository.countByBoardAndIsHeartedTrue(board);

        return new BoardHeartResponseDTO(isHearted, heartCount);
    }

    public boolean isHearted(Long boardSeq, Long userId) {
        BoardEntity board = boardRepository.findById(boardSeq)
                .orElseThrow(() -> new RuntimeException("해당 게시글을 찾을 수 없습니다."));
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 사용자를 찾을 수 없습니다."));

        return boardHeartRepository.findByBoardAndUser(board, user)
                .map(BoardHeartEntity::getIsHearted)
                .orElse(false);
    }

    public int getHeartCount(Long boardSeq) {
        BoardEntity board = boardRepository.findById(boardSeq)
                .orElseThrow(() -> new RuntimeException("해당 게시글을 찾을 수 없습니다."));
        return boardHeartRepository.countByBoardAndIsHeartedTrue(board);
    }
}