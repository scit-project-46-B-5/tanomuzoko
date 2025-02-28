package org.scit.project.mypage.service;

import java.util.Optional;

import org.scit.project.user.entity.UserEntity;
import org.scit.project.user.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MypageService {

	private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
	
    public boolean checkPassword(String storedPassword, String rawPassword) {
        // BCryptPasswordEncoder를 사용해 암호화된 비밀번호와 사용자가 입력한 비밀번호를 비교
        return bCryptPasswordEncoder.matches(rawPassword, storedPassword);
    }

    public boolean updateUserInfo(String username, String newNickName, String newPassword) {
        Optional<UserEntity> optionalUser = userRepository.findByUserId(username);
        
        if (optionalUser.isPresent()) {
            UserEntity user = optionalUser.get();
            user.setUserName(newNickName);
            if (newPassword != null && !newPassword.trim().isEmpty()) {
                user.setUserPassword(passwordEncoder.encode(newPassword));
            }
            
            userRepository.save(user);
            return true;
        }
        return false;
    }
}
