package org.scit.project.user.repository;

import org.scit.project.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long>{



//	아이디 중복확인
	boolean existsByUserId(String userId);
//	닉네임 중복확인
	boolean existsByUserName(String userName);

	

}
