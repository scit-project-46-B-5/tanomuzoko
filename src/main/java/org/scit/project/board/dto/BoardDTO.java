package org.scit.project.board.dto;

import java.time.LocalDateTime;

import org.scit.project.board.entity.BoardEntity;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BoardDTO {
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
    
	public static BoardDTO toDTO(BoardEntity boardEntity) {
		return BoardDTO.builder()
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
