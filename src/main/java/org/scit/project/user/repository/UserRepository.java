package org.scit.project.user.repository;

import java.util.Optional;

import org.scit.project.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long>{



//	Id값으로 사용자 찾기
	Optional<UserEntity> findByUserId(String userId);

//	이메일을 이용해서 아이디를 찾기위한 쿼리메소드
	Optional<UserEntity> findByUserEmail(String userEmail);
	
//	비밀번호를 찾기위한 쿼리메소드
	Optional<UserEntity> findByUserEmailAndUserId(String userEmail, String userId);
	
//	회원 탈퇴 처리된 아이디인지 체크 is_deleted true 검색제외
	boolean existsByUserIdAndIsDeleted(String userId, boolean i); 
	
//	회원 탈퇴 처리된 닉네임인지 체크
	boolean existsByUserNameAndIsDeleted(String userName, boolean i); 
	
//	회원 탈퇴 처리된 이메일인지 체크
	boolean existsByUserEmailAndIsDeleted(String userEmail, boolean i);
	

	

}
