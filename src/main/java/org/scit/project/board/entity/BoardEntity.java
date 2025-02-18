package org.scit.project.board.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.UpdateTimestamp;
import org.scit.project.board.dto.BoardDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@DynamicInsert
@Entity
@Table(name = "board")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class BoardEntity {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_seq")
    private Long boardSeq;

    @Column(name = "user_seq")
    private Long userSeq;

    @Column(name = "board_title", length = 200, columnDefinition = "varchar(200) default 'Untitled'")
    private String boardTitle;

    @Column(name = "board_content", length = 4000)
    private String boardContent;

    @Column(name = "hit_count")
    private Integer hitCount;

    @Column(name = "create_date")
    @CurrentTimestamp
    private LocalDateTime createDate;

    @Column(name = "update_date")
    @UpdateTimestamp
    private LocalDateTime updateDate;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    public static BoardEntity toEntity(BoardDTO boardDTO) {
        return BoardEntity.builder()
                .boardSeq(boardDTO.getBoardSeq() != null ? boardDTO.getBoardSeq() : null)
                .userSeq(boardDTO.getUserSeq())
                .boardTitle(boardDTO.getBoardTitle())
                .boardContent(boardDTO.getBoardContent())
                .hitCount(boardDTO.getHitCount())
                .createDate(boardDTO.getCreateDate())
                .updateDate(boardDTO.getUpdateDate())
                .isDeleted(boardDTO.getIsDeleted())
                .build();
    }

}










