package org.scit.project.reply.service;

import java.util.Optional;

import org.scit.project.board.entity.BoardEntity;
import org.scit.project.board.repository.BoardRepository;
import org.scit.project.reply.dto.ReplyDTO;
import org.scit.project.reply.entity.ReplyEntity;
import org.scit.project.reply.repository.ReplyRepository;
import org.scit.project.user.entity.UserEntity;
import org.scit.project.user.repository.UserRepository;
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
}