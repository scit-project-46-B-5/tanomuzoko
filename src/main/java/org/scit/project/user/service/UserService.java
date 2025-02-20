package org.scit.project.user.service;

import org.scit.project.user.dto.UserDTO;
import org.scit.project.user.entity.UserEntity;
import org.scit.project.user.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder; // ✅ 변수명 오타 수정

    // ✅ 아이디 중복 체크
    public boolean existId(String userId) {
        return !userRepository.existsByUserId(userId);
    }

    // ✅ 닉네임 중복 체크
    public boolean existName(String userName) {
        return !userRepository.existsByUserName(userName);
    }

    // ✅ 이메일 중복 체크
    public boolean isEmailExists(String email) {
        return userRepository.existsByUserEmail(email);
    }

    // ✅ 회원가입 처리
    public boolean joinProc(UserDTO dto) {
        // 1️⃣ 이메일 중복 체크 (이미 존재하면 예외 발생)
        if (isEmailExists(dto.getUserEmail())) {
            throw new IllegalStateException("이미 사용 중인 이메일입니다."); // ✅ 예외 발생
        }

        // 2️⃣ 비밀번호 암호화
        dto.setUserPassword(passwordEncoder.encode(dto.getUserPassword()));

        // 3️⃣ DTO → Entity 변환 후 저장
        UserEntity entity = UserEntity.toEntity(dto);
        userRepository.save(entity);

        // 4️⃣ 저장된 데이터 확인 (회원가입 성공 여부)
        return userRepository.existsByUserId(dto.getUserId());
    }
}
