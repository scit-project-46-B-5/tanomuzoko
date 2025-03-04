package org.scit.project.mypage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MyBoardDto {
    private Long boardSeq;
    private String boardTitle;
    private String boardContent;
    private String originalFileName;
    private Long heartCount;
}
