package com.creavispace.project.domain.tech.service;

import com.creavispace.project.domain.common.dto.FailResponseDto;
import com.creavispace.project.domain.common.dto.SuccessResponseDto;
import com.creavispace.project.domain.recruit.entity.Recruit;
import com.creavispace.project.domain.tech.dto.request.TechStackCreateRequestDto;
import com.creavispace.project.domain.tech.dto.response.TechStackCreateResponseDto;
import com.creavispace.project.domain.tech.entity.TechStack;
import com.creavispace.project.domain.tech.repository.TechStackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TechStackCreateService {

    private final TechStackRepository techStackRepository;

    public ResponseEntity createTechStack(TechStackCreateRequestDto techStackCreateRequestDto) {
        Member member = "jwt토큰을 통해 가져온 멤버";

        //관리자도 아니고 글 작성자도 아니면
        if (!member.getRole().equals("Administrator")) {
            return ResponseEntity.status(401)
                    .body(new FailResponseDto(false, "기술스택을 추가할 수 있는 권한이 없습니다.", 401));
        }


        if(techStackRepository.existsByTechStack(techStackCreateRequestDto.getTechStack())) {
            return ResponseEntity.status(409)
                    .body(new FailResponseDto(false, "해당 기술스택이 이미 존재합니다.", 409));
        }

        TechStack techStack = TechStack.builder()
                .techStack(techStackCreateRequestDto.getTechStack())
                .url(techStackCreateRequestDto.getUrl())
                .build();

        techStackRepository.save(techStack);

        TechStackCreateResponseDto create = TechStackCreateResponseDto.builder()
                .id(techStack.getId())
                .techStack(techStack.getTechStack())
                .url(techStack.getUrl())
                .build();

        return ResponseEntity.ok()
                .body(new SuccessResponseDto(true, "기술 스택이 추가되었습니다.", create));
    }
}
