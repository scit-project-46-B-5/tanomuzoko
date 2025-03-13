package org.scit.project.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EmailVerifyDTO {
    // ✅ 이메일 주소 (Entity의 필드명과 통일)
	@NotBlank(message = "이메일은 필수입니다.")
	@Email(message = "올바른 이메일 형식이어야 합니다.")
    private String userEmail;
	
	@NotBlank(message = "인증번호는 필수입니다.")
	@Size(min = 8, max = 8, message = "인증 코드는 8자리여야 합니다.")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "인증 코드는 알파벳과 숫자만 포함해야 합니다.") // 알파벳과 숫자만 허용
	// ✅ 인증 코드 (Entity에는 없지만 DTO에서는 필요)
    private String verifyCode;
}
