package org.scit.project.board.dto;

import java.time.LocalDateTime;
import org.scit.project.board.entity.BoardEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    
    @NotBlank(message = "제목은 필수입니다.")
    @Size(max = 30, message = "제목은 최대 30자까지 입력 가능합니다.")
    private String boardTitle;

    @NotBlank(message = "내용을 입력해주세요.")
    private String boardContent;    
    
    private Integer hitCount;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private Boolean isDeleted;

    private MultipartFile uploadFile;
    
    @NotBlank(message = "썸네일은 필수입니다.")
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
                .thumbnail(boardEntity.getBoardImageEntity().getOriginalFileName())
                .recipeSeq(boardEntity.getRecipeEntity().getRecipeSeq())
                .build();
    }

    public boolean isThumbnailUrlEmpty() {
        return StringUtils.hasText(this.thumbnailUrl);
    }

    public boolean isUploadedInCurrentBoard() {
        return this.thumbnailUrl.startsWith("/uploads/");
    }
}
