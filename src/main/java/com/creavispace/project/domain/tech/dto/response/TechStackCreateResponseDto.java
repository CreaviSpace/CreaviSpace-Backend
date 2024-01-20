package com.creavispace.project.domain.tech.dto.response;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TechStackCreateResponseDto {
    private Long id;

    private String techStack;

    private String url;
}
