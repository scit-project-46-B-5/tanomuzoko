package org.scit.project.mypage.controller;

import org.scit.project.mypage.service.UserDeleteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class UserDeleteController {

	private final UserDeleteService userDeleteService;
	
	// 회원 탈퇴 페이지
    @GetMapping("/deleteAccount")
    public String deleteAccount() {
    	
    	return "mypage/deleteAccount_mypage";
    }
    
    // 회원 탈퇴 처리
    @PostMapping("/deleteAccountProc")
    public ResponseEntity<Void> deleteAccountProc(@AuthenticationPrincipal UserDetails userDetails, HttpServletRequest request, HttpServletResponse response) {
        try {
            boolean isDeleted = userDeleteService.deleteAccountProc(userDetails.getUsername());
            if(isDeleted) {
            	SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
                logoutHandler.logout(request, response, SecurityContextHolder.getContext().getAuthentication());
            	
                return ResponseEntity.ok().build(); 
            } else {
            	 return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        } catch (Exception e) {
        	log.error("회원 탈퇴 중 오류 발생: ", e); 
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); 
        }
    }
}
