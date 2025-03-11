package org.scit.project.user.dto;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.scit.project.mypage.dto.UserUpdateDTO;
import org.scit.project.user.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class LoginUserDetails implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long userSeq;
	private String userId;
	private String userPassword;
	private String userName;
	private String userEmail;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private boolean isDeleted;
	private String roles;
	
	@Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(this.roles.split(",")).stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
    }
	// 생성자: UserEntity → LoginUserDetails 변환
    public LoginUserDetails(UserEntity entity) {
        this.userSeq = entity.getUserSeq();
        this.userId = entity.getUserId();
        this.userPassword = entity.getUserPassword();
        this.userName = entity.getUserName();
        this.userEmail = entity.getUserEmail();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
        this.isDeleted = entity.isDeleted();
        this.roles = entity.getRoles();
    }

    // update userInfo 시에 Spring Session에 갱신
    public LoginUserDetails(LoginUserDetails loginUserDetails, String encodedPassword, String udpatedUserNickName) {
        this.userSeq = loginUserDetails.getUserSeq();
        this.userId = loginUserDetails.getUserId();
        this.userPassword = encodedPassword;
        this.userName = udpatedUserNickName;
        this.userEmail = loginUserDetails.getUserEmail();
        this.createdAt = loginUserDetails.getCreatedAt();
        this.updatedAt = loginUserDetails.getUpdatedAt();
        this.isDeleted = loginUserDetails.isDeleted();
        this.roles = loginUserDetails.getRoles();
    }

	@Override
	public String getPassword() {
		return this.userPassword;
	}

	@Override
//	아이디 반환
	public String getUsername() {
		return this.userId;
	}
//	닉네임 반환
	public String getUserName() {
		return this.userName;
	}
	
	public Long getUserSeq() {
        return userSeq;
    }
	
	@Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
    	if(this.isDeleted == true) {
    		return false;
    	}
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !isDeleted;
    }
}
