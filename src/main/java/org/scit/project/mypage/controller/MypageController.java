package org.scit.project.mypage.controller;

import java.util.List;

import org.scit.project.mypage.dto.MyReplyDTO;
import org.scit.project.mypage.service.MypageService;
import org.scit.project.user.dto.LoginUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MypageController {

	private final MypageService mypageService;
        
    @GetMapping("/reply")
    public String reply(@AuthenticationPrincipal UserDetails userDetails, Model model) {
    	Long userSeq = ((LoginUserDetails) userDetails).getUserSeq();
    	List<MyReplyDTO> replies = mypageService.getMyReplies(userSeq);
    	model.addAttribute("replies", replies);
    	
    	return "mypage/reply_mypage";
    }
    
}