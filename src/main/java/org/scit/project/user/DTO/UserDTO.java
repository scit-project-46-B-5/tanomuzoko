package org.scit.project.user.DTO;

import java.time.LocalDateTime;

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
	private String userName;
	private String userEmail;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private boolean isDeleted;
	
}

