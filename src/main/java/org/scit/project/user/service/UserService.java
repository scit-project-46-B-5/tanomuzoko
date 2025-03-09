package org.scit.project.user.service;

import java.util.Optional;
import org.scit.project.user.dto.UserDTO;
import org.scit.project.user.entity.UserEntity;
import org.scit.project.user.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder; // ✅ 변수명 오타 수정
    private final EmailService emailService; // ✅ EmailService 주입 추가
   
    // ✅ 아이디 중복 체크
    public boolean existId(String userId) {
        return !userRepository.existsByUserId(userId);
    }

    // ✅ 닉네임 중복 체크
    public boolean existName(String userName) {
        return !userRepository.existsByUserName(userName);
    }

    // ✅ 이메일 중복 체크
    public boolean isEmailExists(String userEmail) {
        return userRepository.existsByUserEmail(userEmail);
    }

    // ✅ 회원가입 처리
    @Transactional				
    public boolean joinProc(UserDTO dto) {
        // 1️⃣ 이메일 중복 체크 (이미 존재하면 예외 발생)
        if (isEmailExists(dto.getUserEmail())) {
            throw new IllegalStateException("이미 사용 중인 이메일입니다."); // ✅ 예외 발생
        }

        // 2️⃣ 비밀번호 암호화
        dto.setUserPassword(passwordEncoder.encode(dto.getUserPassword()));

        // 3️⃣ DTO → Entity 변환 후 저장
        try {
        	UserEntity entity = userRepository.save(UserEntity.toEntity(dto));
        	if (entity != null) {
        		return true;
        	} else {
        		return false;
        	}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
    }
    
//   아이디 찾기
    public String findByUserEmail(String userEmail) {
        return userRepository.findByUserEmail(userEmail)
            .map(UserEntity::getUserId)  // UserEntity에서 userId를 추출
//            .orElseThrow(() -> new NoSuchElementException("해당 이메일을 가진 사용자가 없습니다."));
            .orElse("");
    }

    /**
     * ✅ 비밀번호 찾기 (임시 비밀번호 생성 후 이메일 전송)
     */
    @Transactional
    public boolean resetPasswordAndSendEmail(String userEmail, String userId) {
        log.info("📌 비밀번호 초기화 요청: userId={}, userEmail={}", userId, userEmail);

        // 🔹 1️⃣ 사용자가 존재하는지 확인
        Optional<UserEntity> optionalUser = userRepository.findByUserEmailAndUserId(userEmail, userId);

        if (optionalUser.isEmpty()) {
            log.warn("🚨 해당 이메일과 아이디가 일치하는 사용자가 없습니다.");
            return false; // 존재하지 않는 사용자
        }

        UserEntity user = optionalUser.get();

        // 🔹 2️⃣ 임시 비밀번호 생성 (영문 + 숫자 조합 8자리)
        String temporaryPassword = generateTempPassword(8);
        log.info("🔑 생성된 임시 비밀번호: {}", temporaryPassword);

        // 🔹 3️⃣ 비밀번호 암호화 후 저장
        String encodedPassword = passwordEncoder.encode(temporaryPassword);
        user.setUserPassword(encodedPassword);
        userRepository.save(user); // 변경된 비밀번호 저장
        log.info("✅ 비밀번호 변경 완료 (암호화 적용)");

        // 🔹 4️⃣ 이메일 전송
        String subject = "[SCIT Project] 임시 비밀번호 안내";
        String text = "안녕하세요, " + user.getUserId() + "님\n\n"
                + "귀하의 임시 비밀번호는 [" + temporaryPassword + "] 입니다.\n"
                + "로그인 후 반드시 비밀번호를 변경해주세요.\n\n"
                + "감사합니다.";

        emailService.passwordSend(userEmail, subject, text);
        log.info("📧 임시 비밀번호 이메일 전송 완료: {}", userEmail);

        return true; // 성공적으로 비밀번호 변경 및 이메일 전송 완료
    }

    /**
     * ✅ 랜덤 8자리 임시 비밀번호 생성 (영문 + 숫자 조합)
     */
    private String generateTempPassword(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder tempPassword = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomIndex = (int) (Math.random() * characters.length());
            tempPassword.append(characters.charAt(randomIndex));
        }
        return tempPassword.toString();
    }

}
