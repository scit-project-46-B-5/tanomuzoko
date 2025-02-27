package org.scit.project.reply.dto;

import org.scit.project.reply.entity.ReplyEntity;

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
    private String userId;
    private String replyWriter;
    private String replyContent;

    public static ReplyDTO toDTO(ReplyEntity replyEntity) {
        return ReplyDTO.builder()
            .replySeq(replyEntity.getReplySeq())
            .userId(replyEntity.getUser().getUserId())
            .replyWriter(replyEntity.getUser().getUserName())
            .replyContent(replyEntity.getReplyContent())
            .build();
    }
}
