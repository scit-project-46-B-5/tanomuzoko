package org.scit.project.reply.entity;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.scit.project.board.entity.BoardEntity;
import org.scit.project.reply.dto.ReplyDTO;
import org.scit.project.user.entity.UserEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
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
@Table(name = "reply")
public class ReplyEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_seq")
    private Long replySeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_seq", nullable = false)
    private BoardEntity board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_reply_seq")
    private ReplyEntity parentReply;

    @OneToMany(mappedBy = "parentReply", cascade = CascadeType.ALL)
    @Builder.Default
    private List<ReplyEntity> childReplies = new ArrayList<>();

    @Column(name = "reply_content", nullable = false)
    private String replyContent;

    @Column(name = "is_deleted", nullable = false)
    @Builder.Default
    private Boolean isDeleted = false;

    @Column(name = "create_date")
    @CreationTimestamp
    private LocalDateTime createDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @PreUpdate
    public void onUpdate() {
        this.updateDate = LocalDateTime.now();
    }

    public static ReplyEntity toEntity(ReplyDTO replyDTO, BoardEntity board, UserEntity user, ReplyEntity parentReply) {
        return ReplyEntity.builder()
            .board(board)
            .user(user)
            .replyContent(replyDTO.getReplyContent())
            .parentReply(parentReply)
            .build();
    }
}
