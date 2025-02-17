package org.scit.project.user.entity;

import java.time.LocalDateTime;

import org.scit.project.user.dto.UserDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Entity
@Table(name = "user")
public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="user_seq")
	private Long userSeq;
	
	@Column(name="user_id" ,unique = true)
	private String userId;
	
	@Column(name="user_pwd")
	private String userPwd;
	
	@Column(name="user_name")
	private String userName;
	
	@Column(name="user_email")
	private String userEmail;
	
	@Column(name="created_at")
	private LocalDateTime createdAt;
	
	@Column(name="updated_at")
	private LocalDateTime updatedAt;
	
	@Column(name="is_deleted")
	private boolean isDeleted;
	
	@Column(name="roles")
	private String roles;
	
	public static UserEntity toEntity(UserDTO dto) {
		
		return UserEntity.builder()
				.userSeq(dto.getUserSeq())
				.userId(dto.getUserId())
				.userPwd(dto.getUserPwd())
				.userName(dto.getUserName())
				.userEmail(dto.getUserEmail())
				.createdAt(dto.getCreatedAt()!= null ? dto.getCreatedAt() : LocalDateTime.now())
				.updatedAt(dto.getUpdatedAt()!= null ? dto.getUpdatedAt() : LocalDateTime.now())
				.isDeleted(dto.isDeleted())
				.build();
				
	}
}


