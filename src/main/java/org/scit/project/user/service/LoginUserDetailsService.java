package org.scit.project.user.service;

import org.scit.project.user.dto.LoginUserDetails;
import org.scit.project.user.repository.UserRepository;
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
//	오버라이드는 부모로 부터 상속받은 메소드를 재정의 하는것
//	매개변수명(파라미터) , 접근지정자 보다 큰 지정자로 바꾸는 것만 가능

//	아이디와 비밀번호를 같은 값으로 입력해야 로그인이 되는데 여기서는 아이디만 받기때문에 비밀번호는 비교하지 않아도 된다.
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		return repository.findByUserId(userId)
        .map(LoginUserDetails::new)
        .orElseThrow(() -> new UsernameNotFoundException("ID나 비밀번호가 틀렸습니다."));
	}

}
