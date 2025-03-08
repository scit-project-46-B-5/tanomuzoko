package org.scit.project.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // ✅ CSRF 비활성화 (개발 환경)
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/recipe/**",
                                "/api/v1/email/**",
                                "/posts",
                                "/top-posts",
                                "/top-liked",
                                "/heart/status",
                                "/reply/getReplies",
                                "/board/**",
                                "/user/find-id",
                                "/user/idSearch",
                                "/user/idCheck",
                                "/user/nameCheck",
                                "/user/emailCheck",
                                "/user/join",
                                "/user/joinProc",
                                "/user/login",
                                "/user/loginProc",
                                "/image/**",
                                "/css/**",
                                "/js/**"
                        ).permitAll()
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .requestMatchers("/user/mypage/**").hasAnyRole("ADMIN", "USER")
                        .anyRequest().authenticated())

                // ✅ Custom Login 설정
                .formLogin(auth -> auth
                        .loginPage("/user/login")
                        .loginProcessingUrl("/user/loginProc")
                        .usernameParameter("userId")
                        .passwordParameter("userPassword")
                        .defaultSuccessUrl("/", true) // 로그인 성공 시 메인 페이지 이동
                        .failureUrl("/user/login?error=true")
                        .permitAll())

                // ✅ Logout 설정
                .logout(auth -> auth
                        .logoutUrl("/user/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true));

        return http.build();
    }

    // ✅ 단방향 비밀번호 암호화
    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
