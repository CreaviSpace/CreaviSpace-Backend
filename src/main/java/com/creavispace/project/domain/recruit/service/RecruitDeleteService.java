package com.creavispace.project.domain.recruit.service;

import com.creavispace.project.domain.common.dto.FailResponseDto;
import com.creavispace.project.domain.common.dto.SuccessResponseDto;
import com.creavispace.project.domain.member.entity.Member;
import com.creavispace.project.domain.recruit.dto.request.RecruitDeleteRequestDto;
import com.creavispace.project.domain.recruit.entity.Recruit;
import com.creavispace.project.domain.recruit.repository.RecruitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecruitDeleteService {

    private final RecruitRepository recruitRepository;

    @Transactional
    public ResponseEntity deleteRecruit(RecruitDeleteRequestDto recruitDeleteRequestDto) {
        // JWT 토큰을 통해 유저를 파악하고, 해당 유저가 삭제시킬 권한이 있는지 판단
        Member member = "JWT 토큰을 통해 가져온 멤버";

        // ID로 모집 게시글을 찾음
        Optional<Recruit> optionalRecruit = recruitRepository.findById(recruitDeleteRequestDto.getId());

        // 모집 게시글이 존재하는지 확인
        if (optionalRecruit.isPresent()) {
            Recruit recruit = optionalRecruit.get();

            // 관리자도 아니고 글 작성자도 아니면 권한이 없음
            if (member.getId() != recruit.getMemberId() && !member.getRole().equals("Administrator")) {
                return ResponseEntity.status(401)
                        .body(new FailResponseDto(false, "글을 삭제할 수 있는 권한이 없습니다.", 401));
            }

            // 모집 게시글 삭제
            recruitRepository.deleteById(recruitDeleteRequestDto.getId());

            return ResponseEntity.ok()
                    .body(new SuccessResponseDto(true, "모집 게시글을 삭제하였습니다.", recruitDeleteRequestDto.getId()));
        } else {
            // 해당 ID에 대한 모집 게시글을 찾을 수 없는 경우
            return ResponseEntity.status(404)
                    .body(new FailResponseDto(false, "해당 ID에 대한 모집 게시글을 찾을 수 없습니다.", 404));
        }
    }
}
