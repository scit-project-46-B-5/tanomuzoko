package org.scit.project.user.repository;

import java.util.Optional;

import org.scit.project.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long>{



//	아이디 중복확인
	boolean existsByUserId(String userId);
//	닉네임 중복확인
	boolean existsByUserName(String userName);
//	Id값으로 사용자 찾기
	Optional<UserEntity> findByUserId(String userId);
//	이메일 중복체크
	boolean existsByUserEmail(String email);

	

}
