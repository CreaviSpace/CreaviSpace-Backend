package com.creavispace.project.domain.comment.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RecruitCommentModifyResponseDto {

    private Long id;
    private Long recruitId;
    private Long memberId;
    private LocalDateTime modifiedDate;
    private String content;
}
