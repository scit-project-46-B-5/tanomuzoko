//package org.scit.project.mypage.controller;
//
//import org.scit.project.mypage.dto.MyReplyDTO;
//import org.scit.project.mypage.service.MypageService;
//import org.scit.project.user.dto.LoginUserDetails;
//import org.springframework.data.domain.Page;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//
//@Controller
//@Slf4j
//@RequiredArgsConstructor
//@RequestMapping("/mypage")
//public class MypageController {

//	private final MypageService mypageService;
        
//    @GetMapping("/reply")
//    public String reply(@AuthenticationPrincipal UserDetails userDetails, 
//    		 			@RequestParam(name = "page", defaultValue = "0") int page, 
//    		 			Model model) {
//    	Long userSeq = ((LoginUserDetails) userDetails).getUserSeq();
//    	int pageSize = 8; // 한 페이지에 8개씩 표시
//    	
//    	Page<MyReplyDTO> replies = (Page<MyReplyDTO>) mypageService.getMyReplies(userSeq, page, pageSize);
//    	int totalPages = replies.getTotalPages();
//    	int pageGroupSize = 10; // 페이지 번호 그룹 크기 (10개씩)
//    	int startPage = (page / pageGroupSize) * pageGroupSize;
//    	int endPage = Math.min(startPage + pageGroupSize - 1, totalPages - 1);
//    	    
//    	model.addAttribute("replies", replies);
//        model.addAttribute("currentPage", page);
//        model.addAttribute("totalPages", totalPages);
//        model.addAttribute("startPage", startPage);
//        model.addAttribute("endPage", endPage);
//        
//    	return "mypage/reply_mypage";
//    }
    
//}