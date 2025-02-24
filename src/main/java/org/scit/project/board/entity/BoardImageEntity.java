package org.scit.project.board.entity;

import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.scit.project.board.dto.BoardDTO;
import org.scit.project.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "board_image")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BoardImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_seq")
    private Long imageSeq;

    @ManyToOne
    @JoinColumn(name = "board_seq", nullable = false)
    private BoardEntity boardEntity;

    @Column(name = "original_file_name", length = 255)
    private String originalFileName;

    @Column(name = "saved_file_name", length = 255)
    private String savedFileName;

    @CreationTimestamp
    @Column(name = "create_date", updatable = false)
    private LocalDateTime createDate;

    @UpdateTimestamp
    @Column(name = "update_date")
    private LocalDateTime updateDate;
}
