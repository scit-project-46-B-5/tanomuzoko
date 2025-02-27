package org.scit.project.mypage.controller;



import java.util.Map;

import org.scit.project.mypage.dto.UserUpdateDTO;
import org.scit.project.mypage.service.MypageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MypageController {
	
	private final MypageService mypageService;

    @GetMapping("/recipeSave")
    public String recipeSave() {

        return "mypage/recipeSave_mypage"; 
    }
    
    @GetMapping("/myBoard")
    public String  myBoard() {
    	
    	return "mypage/myBoard_mypage";
    }
    
    @GetMapping("/reply")
    public String reply() {
    	
    	return "mypage/reply_mypage";
    }
    
    @GetMapping("/likedBoard")
    public String likedBoard() {
    	
    	return "mypage/likedBoard_mypage";
    }
    
    @GetMapping("/info")
    public String info(Model model, @AuthenticationPrincipal UserDetails userDetails) {
    	
    	model.addAttribute("user", userDetails);
    	
    	return "mypage/info_mypage";
    }
    
    @PostMapping("/checkPassword")
    @ResponseBody
    public ResponseEntity<Boolean> checkPassword(@RequestBody Map<String, String> request, 
                                                 @AuthenticationPrincipal UserDetails userDetails) {
        String currentPassword = request.get("currentPassword");
        boolean isMatch = mypageService.checkPassword(userDetails.getPassword(), currentPassword);
        
        return ResponseEntity.ok(isMatch);
    }
    
    @PostMapping("/updateInfo")
    public ResponseEntity<?> updateInfo(@RequestBody UserUpdateDTO updateDTO, 
                                        @AuthenticationPrincipal UserDetails userDetails) {
        boolean isUpdated = mypageService.updateUserInfo(userDetails.getUsername(), updateDTO.getNewNickName(), updateDTO.getNewPassword());

        if (isUpdated) {
            return ResponseEntity.ok(Map.of("success", true));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("success", false, "message", "정보 업데이트 실패"));
        }
    }
    
    @GetMapping("deleteAccount")
    public String deleteAccount() {
    	
    	return "mypage/deleteAccount_mypage";
    }
    
}