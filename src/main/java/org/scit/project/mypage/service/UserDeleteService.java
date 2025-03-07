package org.scit.project.mypage.service;

import org.scit.project.user.entity.UserEntity;
import org.scit.project.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDeleteService {

	private final UserRepository userRepository;
	
	@Transactional
	public boolean deleteAccountProc(String username) {
    	try {
    		UserEntity user = userRepository.findByUserId(username)
                    			.orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
       	
            user.setDeleted(true);
   		 	return true;		
    	} catch (Exception e) {
            log.error("회원 탈퇴 중 오류 발생: ", e);
            return false;
        }
	}
}
