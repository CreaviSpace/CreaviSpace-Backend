package com.creavispace.project.domain.position.dto.request;

import com.creavispace.project.domain.member.entity.Member;
import com.creavispace.project.domain.position.dto.response.PositionResponseDto;
import com.creavispace.project.domain.position.entity.Position;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
public class PositionRequestDto {
    private Member memberId;
    private String position;

    public PositionRequestDto(Position position) {
        this.memberId = position.getMemberId();
        this.position = position.getPosition();
    }
}
