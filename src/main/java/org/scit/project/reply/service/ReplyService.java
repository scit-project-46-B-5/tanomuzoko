package org.scit.project.reply.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.scit.project.board.entity.BoardEntity;
import org.scit.project.board.repository.BoardRepository;
import org.scit.project.reply.dto.ReplyDTO;
import org.scit.project.reply.entity.ReplyEntity;
import org.scit.project.reply.repository.ReplyRepository;
import org.scit.project.user.entity.UserEntity;
import org.scit.project.user.repository.UserRepository;
import org.springframework.data.domain.Page;
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

    public void addReply(ReplyDTO replyDTO) {
        Optional<BoardEntity> boardOpt = boardRepository.findById(replyDTO.getBoardSeq());
        if (boardOpt.isEmpty()) {
            throw new IllegalArgumentException("게시글이 존재하지 않습니다.");
        }

        BoardEntity board = boardOpt.get();
        UserEntity user = replyDTO.getUserSeq() != null
                ? userRepository.findById(replyDTO.getUserSeq()).orElse(null)
                : null; // 유저가 없을 경우 null 처리

        ReplyEntity replyEntity = ReplyEntity.toEntity(replyDTO, board, user);

        replyRepository.save(replyEntity);
    }
    
    public Page<ReplyDTO> getReplies(Long boardSeq, int page) {
        Optional<BoardEntity> boardOpt = boardRepository.findById(boardSeq);
        if (boardOpt.isEmpty()) {
            throw new IllegalArgumentException("게시글이 존재하지 않습니다.");
        }

        Page<ReplyEntity> replyPage = replyRepository.findAllByBoardAndIsDeletedFalse(boardOpt.get(), PageRequest.of(page, 10, Sort.by(Sort.Direction.ASC, "createDate")));

        return replyPage.map(ReplyDTO::toDTO);
    }
    
    public void deleteReply(Long replySeq, Long userSeq) {
        Optional<ReplyEntity> replyOpt = replyRepository.findById(replySeq);
        if (replyOpt.isEmpty()) {
            throw new IllegalArgumentException("댓글이 존재하지 않습니다.");
        }
        
        ReplyEntity replyEntity = replyOpt.get();
        if (!replyEntity.getUser().getUserSeq().equals(userSeq)) {
            throw new RuntimeException("본인의 댓글만 삭제할 수 있습니다.");
        }
        
        replyEntity.setIsDeleted(true);
        replyRepository.save(replyEntity);
    }

    public void updateReply(Long replySeq, String replyContent, Long userSeq) {
        Optional<ReplyEntity> replyOpt = replyRepository.findById(replySeq);
        if (replyOpt.isEmpty()) {
            throw new IllegalArgumentException("댓글이 존재하지 않습니다.");
        }

        ReplyEntity replyEntity = replyOpt.get();
        if (!replyEntity.getUser().getUserSeq().equals(userSeq)) {
            throw new RuntimeException("본인의 댓글만 수정할 수 있습니다.");
        }

        replyEntity.setReplyContent(replyContent);
        replyRepository.save(replyEntity);
    }
}