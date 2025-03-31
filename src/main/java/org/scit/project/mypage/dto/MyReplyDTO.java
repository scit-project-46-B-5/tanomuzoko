package org.scit.project.mypage.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
