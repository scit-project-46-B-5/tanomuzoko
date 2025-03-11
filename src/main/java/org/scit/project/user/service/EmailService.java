package org.scit.project.user.service;

import java.util.Random;

import org.scit.project.user.util.RedisUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final RedisUtil redisUtil;
    @Value("${mail.sender.email}")
    private String SENDEREMAIL;
    private final TemplateEngine templateEngine;

    private String createCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    // 이메일 내용 초기화
    private String setContext(String code) {
        Context context = new Context();
        context.setVariable("code", code);
        return templateEngine.process("mail", context);
    }

    // 이메일 폼 생성
    private MimeMessage createEmailForm(String email) throws MessagingException {
        try {
            String authCode = createCode();
            MimeMessage message = javaMailSender.createMimeMessage();
            message.addRecipients(MimeMessage.RecipientType.TO, email);
            message.setSubject("안녕하세요. 인증번호입니다.");
            message.setFrom(SENDEREMAIL);
            message.setText(setContext(authCode), "utf-8", "html");

            String normalizedEmail = email.toLowerCase(); // ✅ 소문자로 변환
            if (redisUtil.existData(normalizedEmail)) {
                redisUtil.deleteData(normalizedEmail);
            }
            redisUtil.setDataExpire(normalizedEmail, authCode, 60 * 30L);

            log.info("📌 Redis에 저장된 인증 코드 [{}]: {}", normalizedEmail, redisUtil.getData(normalizedEmail));

            return message;
        } catch (MessagingException e) {
            log.error("📧 이메일 생성 중 오류 발생: {}", e.getMessage(), e);
            throw e;
        }
    }

    // 인증코드 이메일 발송
    public void sendEmail(String toEmail) {
        try {
            String normalizedEmail = toEmail.toLowerCase();
            MimeMessage emailForm = createEmailForm(normalizedEmail);
            
            log.info("📧 이메일 전송 시작: {}", normalizedEmail);
            javaMailSender.send(emailForm);  // 실제 전송
            log.info("📧 이메일 전송 성공: {}", normalizedEmail);
        } catch (MessagingException e) {
            log.error("📧 이메일 전송 실패 (MessagingException): {} - 오류 메시지: {}", toEmail, e.getMessage(), e);
            throw new RuntimeException("이메일 전송 실패", e);
        } catch (Exception e) {
            log.error("📧 이메일 전송 실패 (기타 예외 발생): {} - 오류 메시지: {}", toEmail, e.getMessage(), e);
            throw new RuntimeException("이메일 전송 중 알 수 없는 오류 발생", e);
        }
    }

 // 코드 검증
    public Boolean verifyEmailCode(String email, String code) {
        String normalizedEmail = email.toLowerCase();
        String codeFoundByEmail = redisUtil.getData(normalizedEmail);

        log.info("📧 [{}] 이메일의 인증 코드 조회: {}", normalizedEmail, codeFoundByEmail);
        log.info("📧 입력된 인증 코드: {}", code);

        if (codeFoundByEmail == null) {
            log.warn("❌ [{}] 이메일의 인증 코드가 Redis에 없음!", normalizedEmail);
            return false;
        }

        boolean isMatch = codeFoundByEmail.equalsIgnoreCase(code.trim()); // 🔹 대소문자 무시하고 비교
        log.info("✅ [{}] 인증 코드 비교 결과: {}", normalizedEmail, isMatch);

        if (isMatch) {
            redisUtil.deleteData(normalizedEmail);
            log.info("✅ [{}] 인증 성공 후 Redis 데이터 삭제 완료!", normalizedEmail);
        } else {
            log.warn("❌ [{}] 인증 실패 - 잘못된 코드 입력!", normalizedEmail);
        }

        return isMatch;
    }
    /**
     * ✅ 비밀번호 찾기 - 임시 비밀번호 전송
     */
    public void passwordSend(String userEmail, String subject, String text) {
        try {
        	log.info("📧 [메일 전송 시도] userEmail={}", userEmail);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(userEmail);
            message.setSubject(subject);
            message.setText(text);
            message.setFrom(SENDEREMAIL); // 발신자 설정
            javaMailSender.send(message); // 이메일 전송
            log.info("📧 비밀번호 이메일 전송 완료: {}", userEmail);
        } catch (Exception e) {
            log.error("🚨 비밀번호 이메일 전송 실패: {}", e.getMessage(), e);
            throw new RuntimeException("비밀번호 이메일 전송 실패", e);
        }
    }

}


