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
@RequiredArgsConstructor
public class BoardHeartService {

    private final BoardHeartRepository boardHeartRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    /**
     * 게시글의 공감(좋아요) 상태를 토글하는 메서드
     * 사용자가 공감을 누르면 추가하고, 이미 누른 상태라면 취소함
     * 
     * @param boardSeq 게시글 ID
     * @param userId   사용자 ID
     * @return BoardHeartResponseDTO (공감 여부, 총 공감 개수)
     */
    @Transactional
    public BoardHeartResponseDTO toggleHeart(Long boardSeq, Long userId) {
        // 게시글과 사용자 정보 조회 (없으면 예외 발생)
        BoardEntity board = boardRepository.findById(boardSeq)
                .orElseThrow(() -> new RuntimeException("해당 게시글을 찾을 수 없습니다."));
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 사용자를 찾을 수 없습니다."));

        // 기존에 사용자가 해당 게시글에 공감을 눌렀는지 확인
        Optional<BoardHeartEntity> heartEntityOptional = boardHeartRepository.findByBoardAndUser(board, user);

        boolean isHearted;
        if (heartEntityOptional.isPresent()) {
            // 공감 정보가 존재하면 공감 상태를 반전시킴
            BoardHeartEntity heartEntity = heartEntityOptional.get();
            heartEntity.toggleHeartStatus(); // 공감 상태 반전 (true → false, false → true)
            isHearted = heartEntity.getIsHearted();
        } else {
            // 공감 정보가 없으면 새로운 공감 엔티티 생성 후 저장
            BoardHeartEntity heartEntity = BoardHeartEntity.toEntity(board, user, true); // ✅ `toEntity` 사용
            boardHeartRepository.save(heartEntity);
            isHearted = true;
        }

        // 현재 게시글의 공감(좋아요) 개수 조회
        int heartCount = boardHeartRepository.countByBoardAndIsHeartedTrue(board);

        // 공감 상태 및 총 공감 개수를 DTO로 반환
        return new BoardHeartResponseDTO(true, isHearted, heartCount);
    }

    /**
     * 사용자가 특정 게시글에 공감(좋아요)을 눌렀는지 여부를 확인하는 메서드
     * 
     * @param boardSeq 게시글 ID
     * @param userId   사용자 ID
     * @return 공감 여부 (true = 공감함, false = 공감하지 않음)
     */
    public boolean isHearted(Long boardSeq, Long userId) {
        // 게시글과 사용자 정보 조회 (없으면 예외 발생)
        BoardEntity board = boardRepository.findById(boardSeq)
                .orElseThrow(() -> new RuntimeException("해당 게시글을 찾을 수 없습니다."));
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 사용자를 찾을 수 없습니다."));

        // 사용자가 해당 게시글에 공감을 눌렀는지 여부 반환
        return boardHeartRepository.findByBoardAndUser(board, user)
                .map(BoardHeartEntity::getIsHearted)
                .orElse(false);
    }

    /**
     * 특정 게시글의 총 공감(좋아요) 개수를 반환하는 메서드
     * 
     * @param boardSeq 게시글 ID
     * @return 총 공감 개수
     */
    public int getHeartCount(Long boardSeq) {
        // 게시글 정보 조회 (없으면 예외 발생)
        BoardEntity board = boardRepository.findById(boardSeq)
                .orElseThrow(() -> new RuntimeException("해당 게시글을 찾을 수 없습니다."));

        // 해당 게시글의 공감(좋아요) 개수 반환
        return boardHeartRepository.countByBoardAndIsHeartedTrue(board);
    }

    /**
     * 특정 게시글의 공감 상태 및 공감 개수를 가져오는 메서드
     * (비회원도 공감 개수는 볼 수 있도록 처리)
     * 
     * @param boardSeq 게시글 ID
     * @param userId   사용자 ID (비회원일 경우 null)
     * @return BoardHeartResponseDTO (로그인 여부, 공감 여부, 총 공감 개수)
     */
    public BoardHeartResponseDTO getHeartStatus(Long boardSeq, Long userId) {
        int heartCount = getHeartCount(boardSeq); // 해당 게시글의 총 공감 개수 조회

        // 비회원일 경우 공감 여부를 false로 설정하고 반환
        if (userId == null) {
            return new BoardHeartResponseDTO(false, false, heartCount);
        }

        // 로그인한 사용자의 공감 여부 확인
        boolean isHearted = isHearted(boardSeq, userId);
        return new BoardHeartResponseDTO(true, isHearted, heartCount);
    }
}