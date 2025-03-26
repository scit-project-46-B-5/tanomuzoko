package org.scit.project.mypage.controller;

import java.util.Map;

import org.scit.project.mypage.dto.UserUpdateDTO;
import org.scit.project.mypage.service.UserInfoService;
import org.scit.project.user.dto.LoginUserDetails;
import org.scit.project.user.dto.UserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class UserInfoController {
	
	private final UserInfoService userInfoService;
	
    // 내 정보 관리 페이지
    @GetMapping("/info")
    public String info(Model model, @AuthenticationPrincipal UserDetails userDetails) {
    	UserDTO user = userInfoService.getUserInfo(userDetails.getUsername());
    	model.addAttribute("user", user);
    	
    	return "mypage/info_mypage";
    }
    
    // 비밀번호 확인
    @PostMapping("/checkPassword")
    @ResponseBody
    public ResponseEntity<Boolean> checkPassword(@RequestBody Map<String, String> request,
                                                 @AuthenticationPrincipal UserDetails userDetails) {
        boolean isMatch = userInfoService.checkPassword(userDetails.getUsername(), request.get("currentPassword"));
        ResponseEntity<Boolean> result = ResponseEntity.ok(isMatch);
        
        return result;
    }
    
    // 회원 정보 업데이트
    @PostMapping("/updateInfo")
    public ResponseEntity<?> updateInfo(@RequestBody UserUpdateDTO updateDTO, 
                                        @AuthenticationPrincipal LoginUserDetails userDetails) {
        String  encodedPassword = userInfoService.updateUserInfo(userDetails.getUsername(), 
                                                        updateDTO.getNewNickName(), 
                                                        updateDTO.getNewPassword());

        LoginUserDetails updatedUserDetails = new LoginUserDetails(
            userDetails, encodedPassword, updateDTO.getNewNickName()
        );

        UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(
            updatedUserDetails, updateDTO.getNewPassword(), userDetails.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(newAuth);

        return ResponseEntity.ok(Map.of("success", true));
    }

	
}
