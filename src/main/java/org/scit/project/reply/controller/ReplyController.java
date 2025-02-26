package org.scit.project.reply.controller;

import java.util.List;

import org.scit.project.reply.dto.ReplyDTO;
import org.scit.project.reply.service.ReplyService;
import org.scit.project.user.dto.LoginUserDetails;
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
    @GetMapping("/getReply")
    public List<ReplyDTO> getReply(@RequestParam(name = "boardSeq") Long boardSeq) {
        List<ReplyDTO> list = replyService.getReply(boardSeq);
        return list;
    }
}
