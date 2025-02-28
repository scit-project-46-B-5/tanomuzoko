package org.scit.project.mainpage.dto;

import org.scit.project.board.entity.BoardEntity;
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
    private String boardTitle;
    private String boardContent;
    private Integer heartCount;

    private String boardImageOriginalFileName;

    public static MainDTO toDTO(BoardEntity boardEntity, int heartCount, String boardImageOriginalFileName) {
        return MainDTO.builder()
                .boardSeq(boardEntity.getBoardSeq())
                .boardTitle(boardEntity.getBoardTitle())
                .boardContent(boardEntity.getBoardContent())
                .heartCount(heartCount)
                .boardImageOriginalFileName(boardImageOriginalFileName)
                .build();
    }
}
