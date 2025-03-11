package org.scit.project.user.dto;

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
public class FindIdResponseDTO {
    private String status;  // 성공 또는 실패 상태
    private String message; // 에러 메시지 또는 성공 메시지
    private String userId;  // 찾은 아이디 (성공 시)
}
