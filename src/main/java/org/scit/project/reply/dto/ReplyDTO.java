package org.scit.project.reply.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.scit.project.reply.entity.ReplyEntity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "댓글 내용은 필수입니다.")
    @Size(min = 1, max = 300, message = "댓글은 1~300자 이내로 입력 가능합니다.")
    private String replyContent;
    private Long parentReplySeq;
    private Boolean isDeleted;
    
    @Builder.Default
    private List<ReplyDTO> childReplies = new ArrayList<>();

    public static ReplyDTO toDTO(ReplyEntity replyEntity) {
        ReplyDTO dto = ReplyDTO.builder()
            .replySeq(replyEntity.getReplySeq())
            .userId(replyEntity.getUser().getUserId())
            .replyWriter(replyEntity.getUser().getUserName())
            .replyContent(replyEntity.getReplyContent())
            .parentReplySeq(replyEntity.getParentReply() != null ? replyEntity.getParentReply().getReplySeq() : null)
            .isDeleted(replyEntity.getIsDeleted())
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
