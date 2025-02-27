package org.scit.project.board_heart.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.scit.project.board.entity.BoardEntity;
import org.scit.project.user.entity.UserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "board_heart", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "board_seq", "user_seq" }) // 같은 유저는 동일 게시글에 한 번만 공감 가능
})
public class BoardHeartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_heart_seq")
    private Long boardHeartSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_seq", nullable = false)
    private BoardEntity board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", nullable = false)
    private UserEntity user;

    @Column(name = "is_hearted", nullable = false)
    @Builder.Default
    private Boolean isHearted = false;

    @Column(name = "create_date", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @PreUpdate
    public void onUpdate() {
        this.updateDate = LocalDateTime.now();
    }

    public void toggleHeartStatus() {
        this.isHearted = !this.isHearted;
    }

    public static BoardHeartEntity toEntity(BoardEntity board, UserEntity user, boolean isHearted) {
        return BoardHeartEntity.builder()
                .board(board)
                .user(user)
                .isHearted(isHearted)
                .build();
    }
}
