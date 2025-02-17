package org.scit.project.board_heart.entity;

import org.scit.project.board.entity.BoardEntity;
import org.scit.project.user.entity.UserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
        @UniqueConstraint(columnNames = { "board_seq", "user_seq" })
})
public class BoardHeartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardHeartSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_seq", insertable = false, updatable = false)
    private BoardEntity board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", insertable = false, updatable = false)
    private UserEntity user;

    @Column(name = "is_hearted", nullable = false)
    private Boolean isHearted = true;
}
