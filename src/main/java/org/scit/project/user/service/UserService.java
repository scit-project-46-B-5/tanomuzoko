package org.scit.project.user.service;

import org.scit.project.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	public boolean existId(String userId) {
		boolean result = userRepository.existsByUserId(userId);
		return !result;
	}

	public boolean existName(String userName) {
		boolean result = userRepository.existsByUserName(userName);
		return !result;
	} 
}
