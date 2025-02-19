package org.scit.project.user.controller;

import java.util.Map;

import org.scit.project.user.dto.UserDTO;
import org.scit.project.user.service.UserService;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
	private final UserService userService;

//	회원가입 화면 출력
	@GetMapping("/join")
	public String join() {
		
		return "user/join";
	}
//	로그인 화면 출력
	@GetMapping("/login")
	public String login(@RequestParam(name="error", required = false)String error,
						@RequestParam(name="errMessage", required = false)String errMessage, Model model) {
		
		model.addAttribute("error", error);
		model.addAttribute("errMessage", errMessage);
		return "user/login";
	}
	
//	아이디 중복체크
	@PostMapping("/idCheck")
	@ResponseBody
	public boolean idCheck (@RequestParam(name="userId")String userId) {
		boolean result =userService.existId(userId);
		return result;
	}
//	아이디 중복체크
	@PostMapping("/nameCheck")
	@ResponseBody
	public boolean nameCheck(@RequestParam(name="userName")String userName) {
		boolean result =userService.existName(userName);
		return result;
	}
	// 이메일 중복 체크
	@PostMapping("/emailCheck")
	@ResponseBody
	public boolean emailCheck(@RequestBody Map<String, String> request) {
	    String email = request.get("userEmail");
	    return userService.isEmailExists(email);
	}
	
//	회원가입 처리요청
	@PostMapping("/joinProc")
	@ResponseBody
	public ResponseEntity<String> joinProc(@RequestBody UserDTO dto) {
	    try {
	        boolean result = userService.joinProc(dto);

	        if (result) {
	            return ResponseEntity.ok("회원가입이 완료되었습니다.");
	        } else {
	            return ResponseEntity.status(400).body("회원가입 실패");
	        }
	    } catch (IllegalStateException e) { // ✅ 예외 발생 시 catch
	        return ResponseEntity.status(400).body(e.getMessage()); // ✅ 중복 이메일 예외 메시지 반환
	    }
	}
}
