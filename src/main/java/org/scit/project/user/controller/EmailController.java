package org.scit.project.user.controller;

import java.util.Map;

import org.scit.project.user.dto.EmailDTO;
import org.scit.project.user.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<Map<String, String>> sendEmail(@RequestBody EmailDTO emailDto) throws MessagingException {
        log.info("EmailController.mailSend() - 이메일 전송 요청: {}", emailDto.getUserEmail());

        if (emailDto.getUserEmail() == null || emailDto.getUserEmail().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "이메일을 입력해주세요."));
        }

        emailService.sendEmail(emailDto.getUserEmail());

        // ✅ JSON 형식으로 응답 반환
        return ResponseEntity.ok(Map.of("message", "인증코드가 발송되었습니다."));
    }


    // ✅ 이메일 인증 코드 검증
    // ✅ 올바르게 Boolean 값을 반환하도록 수정!
    @PostMapping("/verify")
    public ResponseEntity<Boolean> verifyEmail(@RequestBody EmailDTO emailDto) {
        log.info("EmailController.verify() - 이메일 인증 요청: {}", emailDto.getUserEmail());

        if (emailDto.getUserEmail() == null || emailDto.getVerifyCode() == null) {
            return ResponseEntity.badRequest().body(false);  // 🔹 Boolean 값 반환
        }

        boolean isVerified = emailService.verifyEmailCode(emailDto.getUserEmail(), emailDto.getVerifyCode());

        log.info("✅ 최종 인증 결과: {}", isVerified);

        return ResponseEntity.ok(isVerified); // 🔹 Boolean 값 반환
    }
}
