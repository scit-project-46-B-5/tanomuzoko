package org.scit.project.mypage.service;

import org.scit.project.user.dto.UserDTO;
import org.scit.project.user.entity.UserEntity;
import org.scit.project.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserInfoService {
	
	private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final PasswordEncoder passwordEncoder;
    
    // 회원 정보 조회
    public UserDTO getUserInfo(String username) {
        UserEntity user = userRepository.findByUserId(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return UserDTO.toDTO(user);
    }

    // 비밀번호 확인
    public boolean checkPassword(String username, String rawPassword) {
        UserEntity user = userRepository.findByUserId(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return bCryptPasswordEncoder.matches(rawPassword, user.getUserPassword());
    }

    // 회원 정보 수정
    @Transactional
    public boolean updateUserInfo(String username, String newNickName, String newPassword) {
        try {
            UserEntity user = userRepository.findByUserId(username)
                    .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username));

            user.setUserName(newNickName);

            if (newPassword != null && !newPassword.trim().isEmpty()) {
                user.setUserPassword(passwordEncoder.encode(newPassword.trim()));
            }
            return true;
        } catch (Exception e) {
            log.error("사용자 정보 업데이트 중 오류 발생: ", e);
            return false;
        }
    }
}
