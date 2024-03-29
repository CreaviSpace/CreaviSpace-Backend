package com.creavispace.project.domain.recruit.dto.response;

import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeadLineRecruitListReadResponseDto {
    private Long id;
    private String postType;
    private String title;
    private String content;
    private LocalDate end;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private List<RecruitTechStackResponseDto> techStacks;

}
