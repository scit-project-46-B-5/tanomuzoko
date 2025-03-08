package org.scit.project.mypage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MypageController {

        
    @GetMapping("/reply")
    public String reply() {
    	
    	return "mypage/reply_mypage";
    }
    
}