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
    private String userId;
    private String boardTitle;
    private String boardContent;
    private Integer hitCount;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private Boolean isDeleted;

    private MultipartFile uploadFile;
    
    // 드랍존에서 지정한 썸네일 파일의 base64 데이터 (원본_file_name으로 저장됨)
    private String thumbnail;
    
    // 업로드된 파일의 URL (FileService에서 생성한, 원본파일이름과 확장자가 포함된 값)
    private String thumbnailUrl;
    
    private Long recipeSeq;
    
    public static BoardDTO toDTO(BoardEntity boardEntity) {
        return BoardDTO.builder()
                .boardSeq(boardEntity.getBoardSeq())
                .userSeq(boardEntity.getUserEntity().getUserSeq())
                .userId(boardEntity.getUserEntity().getUserId())
                .boardWriter(boardEntity.getUserEntity().getUserName())
                .boardTitle(boardEntity.getBoardTitle())
                .boardContent(boardEntity.getBoardContent())
                .hitCount(boardEntity.getHitCount())
                .createDate(boardEntity.getCreateDate())
                .updateDate(boardEntity.getUpdateDate())
                .isDeleted(boardEntity.getIsDeleted())
                .recipeSeq(boardEntity.getRecipeSeq())
                .build();
    }
}
