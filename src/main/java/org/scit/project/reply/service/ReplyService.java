package org.scit.project.reply.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.scit.project.board.entity.BoardEntity;
import org.scit.project.board.repository.BoardRepository;
import org.scit.project.reply.dto.ReplyDTO;
import org.scit.project.reply.entity.ReplyEntity;
import org.scit.project.reply.repository.ReplyRepository;
import org.scit.project.user.entity.UserEntity;
import org.scit.project.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ReplyService {
    
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final ReplyRepository replyRepository;

    /**
     * 댓글을 추가하는 메서드
     * 
     * @param replyDTO 댓글 정보를 담은 DTO
     */
    public void addReply(ReplyDTO replyDTO) {
        Optional<BoardEntity> boardOpt = boardRepository.findById(replyDTO.getBoardSeq());
        if (boardOpt.isEmpty()) {
            throw new IllegalArgumentException("게시글이 존재하지 않습니다.");
        }

        BoardEntity board = boardOpt.get();
        UserEntity user = replyDTO.getUserSeq() != null
                ? userRepository.findById(replyDTO.getUserSeq()).orElse(null)
                : null; // 유저가 없을 경우 null 처리

        // 부모 댓글이 있을 경우 조회, 없으면 null (답글일 경우 부모 댓글이 존재)
        ReplyEntity parentReply = replyDTO.getParentReplySeq() != null
                ? replyRepository.findById(replyDTO.getParentReplySeq()).orElse(null)
                : null;

        ReplyEntity replyEntity = ReplyEntity.toEntity(replyDTO, board, user, parentReply);

        replyRepository.save(replyEntity);
    }
    
    /**
     * 특정 게시글의 댓글을 조회하는 메서드 (부모 댓글 + 대댓글 포함)
     * 
     * @param boardSeq 게시글 번호
     * @param page     페이지 번호
     * @return 댓글 목록 (Page 객체)
     */
    public Page<ReplyDTO> getReplies(Long boardSeq, int page) {
        Optional<BoardEntity> boardOpt = boardRepository.findById(boardSeq);
        if (boardOpt.isEmpty()) {
            throw new IllegalArgumentException("게시글이 존재하지 않습니다.");
        }

        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.ASC, "createDate"));

        // Fetch paginated parent and child replies in a single query
        Page<ReplyEntity> replyPage = replyRepository.findRepliesByBoard(boardOpt.get(), pageable);

        // Convert to DTOs
        List<ReplyDTO> replyDTOs = replyPage.getContent().stream()
                .map(ReplyDTO::toDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(replyDTOs, pageable, replyPage.getTotalElements());
    }
    
    /**
     * 댓글 삭제 메서드 (본인 댓글만 삭제 가능)
     * 
     * @param replySeq 댓글 ID
     * @param userSeq  삭제 요청한 유저 ID
     */
    public void deleteReply(Long replySeq, Long userSeq) {
        Optional<ReplyEntity> replyOpt = replyRepository.findById(replySeq);
        if (replyOpt.isEmpty()) {
            throw new IllegalArgumentException("댓글이 존재하지 않습니다.");
        }
        ReplyEntity replyEntity = replyOpt.get();

        // 현재 로그인한 사용자의 ID와 댓글 작성자의 ID가 다르면 삭제 불가
        if (!replyEntity.getUser().getUserSeq().equals(userSeq)) {
            throw new RuntimeException("본인의 댓글만 삭제할 수 있습니다.");
        }

        replyEntity.setIsDeleted(true); // 논리적 삭제 처리 (실제 삭제하지 않고 isDeleted 필드 변경)
        replyRepository.save(replyEntity);
    }

    /**
     * 댓글 수정 메서드 (본인 댓글만 수정 가능)
     * 
     * @param replySeq     댓글 ID
     * @param replyContent 수정할 댓글 내용
     * @param userSeq      수정 요청한 유저 ID
     */
    public void updateReply(Long replySeq, String replyContent, Long userSeq) {
        Optional<ReplyEntity> replyOpt = replyRepository.findById(replySeq);
        if (replyOpt.isEmpty()) {
            throw new IllegalArgumentException("댓글이 존재하지 않습니다.");
        }
        ReplyEntity replyEntity = replyOpt.get();

        // 현재 로그인한 사용자의 ID와 댓글 작성자의 ID가 다르면 수정 불가
        if (!replyEntity.getUser().getUserSeq().equals(userSeq)) {
            throw new RuntimeException("본인의 댓글만 수정할 수 있습니다.");
        }

        replyEntity.setReplyContent(replyContent);
        replyRepository.save(replyEntity);
    }
}