package org.scit.project.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

//	회원가입 화면 출력
	@GetMapping("/join")
	public String join() {
		
		return "user/join";
	}
	@GetMapping("/login")
	public String login() {
		
		return "user/login";
	}
	
}
