package org.scit.project.mypage.dto;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyBoardDto {
    private Long boardSeq;
    private String boardTitle;
    private String boardContent;
    private String originalFileName;
    private Long heartCount;
    
    public String getFirstLineContent() {
        if (boardContent == null || boardContent.isEmpty()) {
            return "";
        }

        // HTML을 파싱하여 텍스트만 추출
        Document doc = Jsoup.parse(boardContent);
        String textContent = doc.text();

        // 개행 문자 기준으로 첫 줄만 가져오기
        String[] lines = textContent.split("\\n");
        return lines.length > 0 ? lines[0] : textContent;
    }
}
