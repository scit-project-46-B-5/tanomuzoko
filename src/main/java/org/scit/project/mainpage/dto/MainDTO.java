package org.scit.project.mainpage.dto;

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
public class MainDTO {
    private Integer sampleId;
    private String sampleNmae;
}
