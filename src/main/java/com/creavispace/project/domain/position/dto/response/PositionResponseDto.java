package com.creavispace.project.domain.position.dto.response;

import com.creavispace.project.domain.member.entity.Member;
import com.creavispace.project.domain.position.dto.request.PositionRequestDto;
import com.creavispace.project.domain.position.entity.Position;
import lombok.Data;

@Data
public class PositionResponseDto {
    private String position;

    public PositionResponseDto(String position) {
        this.position = position;
    }

    public PositionResponseDto(Position position) {
        this.position = position.getPosition();
    }
}
