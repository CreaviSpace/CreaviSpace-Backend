package com.creavispace.project.domain.tech.dto.request;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TechStackCreateRequestDto {

    private String techStack;

    private String url;
}
