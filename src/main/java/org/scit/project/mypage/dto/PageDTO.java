package org.scit.project.mypage.dto;

import java.util.List;

import org.springframework.data.domain.Page;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Builder
@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class PageDTO<T> {
    private List<T> content;
    private int pageNumber;
    private int totalPages;
    private long totalElements;

    public static <T> PageDTO<T> TODTO(Page<T> page) {
        return PageDTO.<T>builder().content(page.getContent())
                                    .pageNumber(page.getNumber())
                                    .totalElements(page.getTotalElements())
                                    .totalPages(page.getTotalPages())
                                    .build();
    }
}
