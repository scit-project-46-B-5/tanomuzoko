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

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MainDTO {

    private Long boardSeq;
    private String boardWriter;
    private Long userSeq;
    private String boardTitle;
    private String boardContent;
    private Integer hitCount;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private Boolean isDeleted;

	private MultipartFile uploadFile;
    
	public static MainDTO toDTO(BoardEntity boardEntity) {
		return MainDTO.builder()
                .boardSeq(boardEntity.getBoardSeq())
                .boardWriter(boardEntity.getUserEntity().getUserName())
                .boardTitle(boardEntity.getBoardTitle())
                .boardContent(boardEntity.getBoardContent())
                .hitCount(boardEntity.getHitCount())
                .createDate(boardEntity.getCreateDate())
                .updateDate(boardEntity.getUpdateDate())
                .isDeleted(boardEntity.getIsDeleted())
                .build();
	}
}
