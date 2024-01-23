package com.creavispace.project.domain.position.service;

import com.creavispace.project.domain.member.repository.MemberRepository;
import com.creavispace.project.domain.position.dto.request.PositionRequestDto;
import com.creavispace.project.domain.position.dto.response.PositionResponseDto;
import com.creavispace.project.domain.position.entity.Position;
import com.creavispace.project.domain.position.repository.PositionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PositionServiceImpl implements PositionService{

    private final PositionRepository positionRepository;
    private final MemberRepository memberRepository;
    @Override
    public PositionResponseDto savePosition(PositionRequestDto dto) {
        if (positionRepository.existsByMemberId(dto.getMemberId())) {
            updatePosition(dto);
            return new PositionResponseDto(dto.getPosition());
        }
        Position position = new Position(dto);
        positionRepository.save(position);
        return new PositionResponseDto(position.getPosition());
    }

    @Override
    public void updatePosition(PositionRequestDto dto) {
        if (!positionRepository.existsByMemberId(dto.getMemberId())) {
            savePosition(dto);
            return;
        }
        Position position = positionRepository.findByMemberId(dto.getMemberId()).orElseThrow();
        position.setPosition(dto.getPosition());
        positionRepository.save(position);
    }

    @Override
    public void deletePosition(PositionRequestDto dto) {
        Position position = positionRepository.findByMemberId(dto.getMemberId()).orElseThrow();
        if (positionRepository.existsByPositionId(position.getPositionId())) {
            positionRepository.delete(position);
        }
    }

    @Override
    public PositionResponseDto readPosition(PositionRequestDto dto) {
        Position position;
        if (positionRepository.existsByMemberId(dto.getMemberId())) {
            position = positionRepository.findByMemberId(dto.getMemberId()).orElseThrow();
            return new PositionResponseDto(position.getPosition());
        }
        return new PositionResponseDto("없음");
    }

    public List<Position> readByPosition(String position) {
        List<Position> positions = positionRepository.findByPosition(position);
        return positions;
    }
}
