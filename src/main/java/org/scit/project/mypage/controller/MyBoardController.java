package org.scit.project.mypage.controller;

import java.util.List;

import org.scit.project.mypage.dto.MyBoardDto;
import org.scit.project.mypage.service.MypageService;
import org.scit.project.user.dto.LoginUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MyBoardController {

	private final MypageService mypageService;
	
    // 사용자가 쓴 게시물 조회
    @GetMapping("/myBoard")
    public String  myBoard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
    	Long userSeq = ((LoginUserDetails) userDetails).getUserSeq();
    	List<MyBoardDto> myBoard = mypageService.getMyBoards(userSeq, 0 , 4);
    	model.addAttribute("myBoard", myBoard);
    	
    	return "mypage/myBoard_mypage";
    }
    

	 // 사용자가 쓴 게시물 추가 요청 
	 @GetMapping("/myBoard/more")
	 @ResponseBody
	 public List<MyBoardDto> loadMoreBoards(@AuthenticationPrincipal UserDetails userDetails, 
	                                        @RequestParam(name = "page") int page) {
	     Long userSeq = ((LoginUserDetails) userDetails).getUserSeq();
	     
	     return mypageService.getMyBoards(userSeq, page, 4);
	 }
	 
	 // 좋아요 한 게시물 조회
	 @GetMapping("/likedBoard")
	 public String likedBoard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
		 Long userSeq = ((LoginUserDetails) userDetails).getUserSeq();
		 List<MyBoardDto> likedBoards = mypageService.getLikedBoards(userSeq, 0 , 4);
		 model.addAttribute("likedBoards", likedBoards);
	   	
	 	return "mypage/likedBoard_mypage";
	   }
	 
	 // 좋아요 한 게시물 추가 요청
	 @GetMapping("/likedBoard/more")
	 @ResponseBody
	 public List<MyBoardDto> loadMoreLikedBoards(@AuthenticationPrincipal UserDetails userDetails, 
	                                        	 @RequestParam(name = "page") int page) {
	     Long userSeq = ((LoginUserDetails) userDetails).getUserSeq();
	     
	     return mypageService.getLikedBoards(userSeq, page, 4);
	 }
}
