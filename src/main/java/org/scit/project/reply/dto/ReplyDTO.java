package org.scit.project.reply.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    private Long parentReplySeq;
    
    @Builder.Default
    private List<ReplyDTO> childReplies = new ArrayList<>();

    public static ReplyDTO toDTO(ReplyEntity replyEntity) {
        ReplyDTO dto = ReplyDTO.builder()
            .replySeq(replyEntity.getReplySeq())
            .userId(replyEntity.getUser().getUserId())
            .replyWriter(replyEntity.getUser().getUserName())
            .replyContent(replyEntity.getReplyContent())
            .parentReplySeq(replyEntity.getParentReply() != null ? replyEntity.getParentReply().getReplySeq() : null)
            .build();
            
        if (!replyEntity.getChildReplies().isEmpty()) {
            dto.setChildReplies(
                    replyEntity.getChildReplies().stream()
                            .map(ReplyDTO::toDTO)
                            .collect(Collectors.toList())
            );
        }
        
        return dto;
    }
}
