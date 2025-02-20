package org.scit.project.user.dto;

import lombok.Data;

@Data
public class EmailDTO {
    // ✅ 이메일 주소 (Entity의 필드명과 통일)
    private String email;

    // ✅ 인증 코드 (Entity에는 없지만 DTO에서는 필요)
    private String verifyCode;
}
