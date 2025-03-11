package org.scit.project.mypage.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MyReplyDTO {
	private Long boardSeq;
	private String boardTitle;
	private String replyContent;
	private LocalDateTime createDate;
}
