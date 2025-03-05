package org.scit.project.reply.controller;

import java.util.List;

import org.scit.project.reply.dto.ReplyDTO;
import org.scit.project.reply.service.ReplyService;
import org.scit.project.user.dto.LoginUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/reply")
@RequiredArgsConstructor
public class ReplyController {
    
    private final ReplyService replyService;

    // 댓글 등록
    @PostMapping("/addReply")
    public void addReply(@ModelAttribute ReplyDTO replyDTO, @AuthenticationPrincipal LoginUserDetails loginUserDetails) {
        Long userSeq = loginUserDetails.getUserSeq();
        replyDTO.setUserSeq(userSeq);
        replyService.addReply(replyDTO);
    }

    // 댓글 출력
    @GetMapping("/getReplies")
    public Page<ReplyDTO> getReplies(@RequestParam(name = "boardSeq") Long boardSeq, 
                                     @RequestParam(name = "page", defaultValue = "0") int page) {
        
        return replyService.getReplies(boardSeq, page);
    }

    // 댓글 삭제
    @PostMapping("/deleteReply")
    public void deleteReply(@RequestParam(name = "replySeq") Long replySeq, 
                            @AuthenticationPrincipal LoginUserDetails loginUserDetails) {
        Long userSeq = loginUserDetails.getUserSeq();
        replyService.deleteReply(replySeq, userSeq);
    }

    // 댓글 수정
    @PostMapping("/updateReply")
    public void updateReply(@RequestParam(name = "replySeq") Long replySeq,
                            @RequestParam("replyContent") String replyContent,
                            @AuthenticationPrincipal LoginUserDetails loginUserDetails) {
        Long userSeq = loginUserDetails.getUserSeq();
        replyService.updateReply(replySeq, replyContent, userSeq);
    }
}
