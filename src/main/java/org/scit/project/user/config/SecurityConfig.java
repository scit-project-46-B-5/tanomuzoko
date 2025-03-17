package org.scit.project.user.config;


import org.scit.project.user.handler.CustomAuthenticationFailureHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final CustomAuthenticationFailureHandler failureHandler;
	
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // ✅ CSRF 비활성화 (개발 환경)
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/api/v1/email/**",
                                "/posts",
                                "/top-posts",
                                "/top-liked",
                                "/heart/status",
                                "/reply/getReplies",
                                "/board/board",
                                "/board/boardDetail",
                                "/board/popularPostsAjax",
                                "/user/restore",
                                "/user/find-password",
                                "/user/passwordSearch",
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
                        .requestMatchers("/mypage/**").hasAnyRole( "USER")
                        .anyRequest().authenticated())

                // ✅ Custom Login 설정
                .formLogin(auth -> auth
                        .loginPage("/user/login")
                        .loginProcessingUrl("/user/loginProc")
                        .usernameParameter("userId")
                        .passwordParameter("userPassword")
                        .defaultSuccessUrl("/", true) // 로그인 성공 시 메인 페이지 이동
                        .failureHandler(failureHandler) // 로그인 실패 시 CustomAuthenticationFailureHandler 사용
//                        .failureUrl("/user/login?error=true")
                        .permitAll())

                // ✅ Logout 설정
                .logout(auth -> auth
                        .logoutUrl("/user/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true))
		             
		             // ✅ 예외 처리 추가 (접근 거부)
		                .exceptionHandling(exception -> exception
		                        .accessDeniedHandler((request, response, accessDeniedException) -> {
		                            response.sendRedirect("/user/login?errorType=forbidden");
		                        })
		                );
        

        return http.build();
    }

    // ✅ 단방향 비밀번호 암호화
    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
