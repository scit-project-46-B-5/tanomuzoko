package org.scit.project.user.handler;

import java.io.IOException;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String redirectUrl = "/user/login?errorType=";
        // 로그인 시도한 사용자 ID 가져오기
        String userId = request.getParameter("userId");
        
        if (exception instanceof InternalAuthenticationServiceException) {
        	System.out.println("aaa");
        	System.out.println(request.getParameter("userId"));
        } else if (exception instanceof DisabledException) {
        	System.out.println("bbb");
        }

        try {
            // 예외가 발생할 수 있는 코드
            throw exception;  // 여기서 exception을 던져서 catch 문에서 처리하게 만듭니다.
        } catch (InternalAuthenticationServiceException e) {
            // DisabledException 처리
            log.debug("🚨 비활성화된 계정으로 로그인 시도: {}", request.getParameter("userId"));
            redirectUrl = "/user/restore?errorType=disabled&userId=" + userId;  // userId를 포함하여 리다이렉트
        } catch (BadCredentialsException e) {
            // BadCredentialsException 처리
            log.debug("🚨 잘못된 자격 증명으로 로그인 시도");
            redirectUrl += "badCredentials";  // 잘못된 자격 증명 처리
        } catch (Exception e) {
            // 그 외의 예외 처리
            log.debug("🚨 기타 인증 실패: {}", e.getMessage());
            redirectUrl += "unauthorized";  // 기타	 인증 실패 처리
        }

        // 최종적으로 리다이렉트
        response.sendRedirect(redirectUrl);
    }


}
