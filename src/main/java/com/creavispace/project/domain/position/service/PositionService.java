package com.creavispace.project.domain.position.service;

import com.creavispace.project.domain.position.dto.request.PositionRequestDto;
import com.creavispace.project.domain.position.dto.response.PositionResponseDto;
import com.creavispace.project.domain.position.entity.Position;
import java.util.List;

public interface PositionService {
    PositionResponseDto savePosition(PositionRequestDto dto);

    void updatePosition(PositionRequestDto dto);

    void deletePosition(PositionRequestDto dto);

    PositionResponseDto readPosition(PositionRequestDto dto);
}
