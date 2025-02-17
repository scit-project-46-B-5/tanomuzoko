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
	private final BCryptPasswordEncoder passwprdEncoder;

	public boolean existId(String userId) {
		boolean result = userRepository.existsByUserId(userId);
		return !result;
	}

	public boolean existName(String userName) {
		boolean result = userRepository.existsByUserName(userName);
		return !result;
	}

	//	회원가입 처리
	public boolean joinProc(UserDTO dto) {
//		비밀번호 암호화
	dto.setUserPassword(passwprdEncoder.encode(dto.getUserPassword()));
		 
		// DTO를 Entity로 변환하여 DB에 저장
		UserEntity entity = UserEntity.toEntity(dto);
		userRepository.save(entity);

		// 저장 후 DB에 아이디가 존재하는지 확인
		boolean result = userRepository.existsByUserId(dto.getUserId());
		return result;	// 가입 성공 여부 반환
	}
	
}
