package org.scit.project.user.controller;

import java.util.Map;

import org.scit.project.user.dto.EmailCheckDTO;
import org.scit.project.user.dto.EmailVerifyDTO;
import org.scit.project.user.service.EmailService;
import org.scit.project.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;
    private final UserService userService;  // ✅ `UserService`를 주입

    @PostMapping("/api/v1/email/send")
    public ResponseEntity<Map<String, String>> sendEmail(@RequestBody @Valid EmailCheckDTO emailDto) throws MessagingException {
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
    @PostMapping("/api/v1/email/verify")
    public ResponseEntity<Boolean> verifyEmail(@RequestBody @Valid EmailVerifyDTO emailVerifyDTO) {
        log.info("EmailController.verify() - 이메일 인증 요청: {}", emailVerifyDTO.getUserEmail());

        if (emailVerifyDTO.getUserEmail() == null || emailVerifyDTO.getVerifyCode() == null) {
            return ResponseEntity.badRequest().body(false);  // 🔹 Boolean 값 반환
        }

        boolean isVerified = emailService.verifyEmailCode(emailVerifyDTO.getUserEmail(), emailVerifyDTO.getVerifyCode());

        log.info("✅ 최종 인증 결과: {}", isVerified);

        return ResponseEntity.ok(isVerified); // 🔹 Boolean 값 반환
    }
 // 비밀번호 찾기 요청 처리
    @PostMapping("/user/find-password")
    public ResponseEntity<Map<String, String>> passwordSearch(@RequestBody Map<String, String> requestData) {
        String userEmail = requestData.get("userEmail");
        String userId = requestData.get("userId");

        log.info("📌 비밀번호 찾기 요청: userId={}, userEmail={}", userId, userEmail);

        // 🔹 아이디 또는 이메일이 입력되지 않은 경우
        if (userEmail == null || userId == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "아이디 또는 이메일을 입력해주세요."));
        }

        // 🔹 `UserService`에서 비밀번호 초기화 및 이메일 전송 실행
        boolean result = userService.resetPasswordAndSendEmail(userEmail, userId);

        // 🔹 사용자가 존재하지 않으면 오류 메시지 반환
        if (!result) {
            return ResponseEntity.badRequest().body(Map.of("message", "아이디 또는 이메일이 일치하지 않습니다."));
        }
        
        // 🔹 성공 메시지 반환
        return ResponseEntity.ok(Map.of("message", "임시 비밀번호가 이메일로 전송되었습니다."));
    }
}
