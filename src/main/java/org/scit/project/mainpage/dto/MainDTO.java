package org.scit.project.mainpage.dto;

import java.time.LocalDateTime;

import org.scit.project.board.entity.BoardEntity;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Setter
@Getter
public class MainDTO {
    private Long boardSeq;
    private String boardWriter;
    private String boardTitle;
    private String boardContent;
    private Integer hitCount;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private String originalFileName;
    private String savedFileName;
    private Boolean isDeleted;

    private MultipartFile uploadFile;

    public static MainDTO toDTO(BoardEntity boardEntity) {
        return MainDTO.builder()
                .boardSeq(boardEntity.getBoardSeq())
                .boardWriter(boardEntity.getBoardWriter())
                .boardTitle(boardEntity.getBoardTitle())
                .boardContent(boardEntity.getBoardContent())
                .hitCount(boardEntity.getHitCount())
                .createDate(boardEntity.getCreateDate())
                .updateDate(boardEntity.getUpdateDate())
                .originalFileName(boardEntity.getOriginalFileName())
                .savedFileName(boardEntity.getSavedFileName())
                .isDeleted(boardEntity.getIsDeleted())
                .build();
    }
}
