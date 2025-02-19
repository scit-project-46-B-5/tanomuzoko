package org.scit.project.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "email", uniqueConstraints = {
    @UniqueConstraint(columnNames = "email")  // ✅ 이메일 중복 방지
})
public class EmailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "email_id", unique = true, nullable = false)
    private Long id;  // ✅ 자동 증가 ID (PK)

    @Column(name = "email", nullable = false, unique = true)
    private String email;  // ✅ 이메일 주소 (중복 방지)

    @Column(name = "email_status", nullable = false)
    private boolean emailStatus;  // ✅ 이메일 인증 여부

    // 📌 **올바른 생성자 사용**
    @Builder
    public EmailEntity(String email) {
        this.email = email;
        this.emailStatus = false;  // 기본값: 인증되지 않음
    }

    // ✅ 이메일 인증 여부 업데이트 메서드 추가
    public void setEmailStatus(boolean status) {
        this.emailStatus = status;
    }
}
