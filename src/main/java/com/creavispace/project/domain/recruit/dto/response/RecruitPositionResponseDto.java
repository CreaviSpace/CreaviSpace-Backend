package com.creavispace.project.domain.recruit.dto.response;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RecruitPositionResponseDto {

    private String position;

    private int amount;

    private int now;

    private boolean status;

}
