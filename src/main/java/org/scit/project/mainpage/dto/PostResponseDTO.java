package org.scit.project.mainpage.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PostResponseDTO {

    private List<MainDTO> posts;
    private boolean isLastPage;
}
