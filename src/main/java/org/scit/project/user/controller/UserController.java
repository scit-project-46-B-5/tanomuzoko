package org.scit.project.user.controller;

import org.scit.project.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
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
	public String login() {
		
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
//	회원가입 처리
	@PostMapping("/joinProc")
	public String joinProc(@ModelAttribute org.scit.project.user.dto.UserDTO dto) {
		boolean result = userService.joinProc(dto);
		return "redirect:/";
	}

}
