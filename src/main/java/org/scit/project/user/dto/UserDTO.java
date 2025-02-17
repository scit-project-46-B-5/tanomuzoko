package org.scit.project.user.dto;

import java.time.LocalDateTime;

import org.scit.project.user.entity.UserEntity;

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
public class UserDTO {
	
	private Long userSeq;
	private String userId;
	private String userPassword;
	private String userName;
	private String userEmail;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private boolean isDeleted;
	@Builder.Default
	private String roles = "ROLE_USER";	
	
	public static UserDTO toDTO(UserEntity entity) {
		
		return UserDTO.builder()
				.userSeq(entity.getUserSeq())
				.userId(entity.getUserId())
//				.userPassword(entity.get())
				.userName(entity.getUserName())
				.userEmail(entity.getUserEmail())
				.createdAt(entity.getCreatedAt()!= null ? entity.getCreatedAt() : LocalDateTime.now())
				.updatedAt(entity.getUpdatedAt()!= null ? entity.getCreatedAt() : LocalDateTime.now())
				.isDeleted(entity.isDeleted())
				.roles(entity.getRoles())
				.build();
				
	}
}

