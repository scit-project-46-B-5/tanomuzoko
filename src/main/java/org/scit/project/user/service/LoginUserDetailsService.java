package org.scit.project.user.service;

import org.scit.project.user.dto.LoginUserDetails;
import org.scit.project.user.entity.UserEntity;
import org.scit.project.user.repository.UserRepository;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoginUserDetailsService implements UserDetailsService {
    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        // 🔹 데이터베이스에서 사용자 조회
        UserEntity user = repository.findByUserId(username)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 아이디입니다."));

        // ✅ 계정이 비활성화된 경우 예외 발생
        if (user.isDeleted()) {
            log.warn("🚨 로그인 실패2 - 비활성화된 계정: {}", username);
            throw new DisabledException("비활성화된 계정입니다.");
        }

        return new LoginUserDetails(user);
    }
}
