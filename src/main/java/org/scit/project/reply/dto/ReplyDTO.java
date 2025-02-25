package org.scit.project.reply.dto;

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
public class ReplyDTO {
    
    private Long replySeq;
    private Long boardSeq;
    private Long userSeq;
    private String replyContent;
}
