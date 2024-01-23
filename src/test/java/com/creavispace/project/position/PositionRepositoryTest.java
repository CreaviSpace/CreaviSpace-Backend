package com.creavispace.project.position;

import com.creavispace.project.domain.member.entity.Member;
import com.creavispace.project.domain.member.repository.MemberRepository;
import com.creavispace.project.domain.position.dto.request.PositionRequestDto;
import com.creavispace.project.domain.position.dto.response.PositionResponseDto;
import com.creavispace.project.domain.position.entity.Position;
import com.creavispace.project.domain.position.repository.PositionRepository;
import com.creavispace.project.domain.position.service.PositionServiceImpl;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
public class PositionRepositoryTest {

    @Autowired
    PositionRepository positionRepository;
    @Autowired
    MemberRepository memberRepository;

    @Test
    @Commit
    void savePosition() {
        PositionServiceImpl positionService = new PositionServiceImpl(positionRepository, memberRepository);

        Member member = memberRepository.findById(2L).orElseThrow();

        PositionRequestDto dto = new PositionRequestDto();
        dto.setMemberId(member);
        dto.setPosition("백엔드");
        positionService.savePosition(dto);
    }

    @Test
    @Commit
    void updatePosition() {
        PositionServiceImpl positionService = new PositionServiceImpl(positionRepository, memberRepository);

        Member member = memberRepository.findById(1L).orElseThrow();
        PositionRequestDto dto = new PositionRequestDto();
        dto.setMemberId(member);
        dto.setPosition("프론트엔드");
        positionService.updatePosition(dto);
    }
    @Test
    @Commit
    void deletePosition() {
        PositionServiceImpl positionService = new PositionServiceImpl(positionRepository, memberRepository);

        Member member = memberRepository.findById(1L).orElseThrow();
        PositionRequestDto dto = new PositionRequestDto();
        dto.setMemberId(member);
        dto.setPosition("프론트엔드");
        positionService.deletePosition(dto);
    }

    @Test
    void readPosition() {
        PositionServiceImpl positionService = new PositionServiceImpl(positionRepository, memberRepository);
        PositionRequestDto dto = new PositionRequestDto();
        Member member = memberRepository.findById(2L).orElseThrow();
        dto.setMemberId(member);
        System.out.println(positionService.readPosition(dto));
    }

    @Test
    void readByPosition() {
        PositionServiceImpl positionService = new PositionServiceImpl(positionRepository, memberRepository);
        List<Position> positions = positionService.readByPosition("백엔드");
        positions.forEach(p -> System.out.println(new PositionRequestDto(p)));
    }
}
