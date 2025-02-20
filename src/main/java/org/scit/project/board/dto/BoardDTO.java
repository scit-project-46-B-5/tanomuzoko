package org.scit.project.board.dto;

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
public class BoardDTO {

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
    
    // 드랍존에서 지정한 썸네일 파일의 URL 또는 파일명을 저장할 필드
    private String thumbnail;
    
    public static BoardDTO toDTO(BoardEntity boardEntity) {
        return BoardDTO.builder()
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
