package com.creavispace.project.domain.comment.dto.request;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RecruitCommentDeleteRequestDto {
    private long id;
}
