package com.creavispace.project.domain.recruit.service;

import com.creavispace.project.domain.common.dto.FailResponseDto;
import com.creavispace.project.domain.common.dto.SuccessResponseDto;
import com.creavispace.project.domain.member.entity.Member;
import com.creavispace.project.domain.member.repository.MemberRepository;
import com.creavispace.project.domain.recruit.dto.request.RecruitCreateRequestDto;
import com.creavispace.project.domain.recruit.dto.response.RecruitCreateResponseDto;
import com.creavispace.project.domain.recruit.dto.response.RecruitPositionResponseDto;
import com.creavispace.project.domain.recruit.entity.Recruit;
import com.creavispace.project.domain.recruit.entity.RecruitPosition;
import com.creavispace.project.domain.recruit.repository.RecruitPositionRepository;
import com.creavispace.project.domain.recruit.repository.RecruitRepository;
import com.creavispace.project.global.util.TimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecruitCreateService {

    private final RecruitRepository recruitRepository;
    private final RecruitPositionRepository recruitPositionRepository;
    private final TimeUtil timeUtil;
    private final MemberRepository memberRepository;

    @Transactional
    public ResponseEntity createRecruit(RecruitCreateRequestDto recruitCreateRequestDto) {
        // 회원 ID로 회원을 찾음
        Optional<Member> optionalMember = memberRepository.findById(recruitCreateRequestDto.getMemberId());

        if (optionalMember.isPresent()) {
            Member member = optionalMember.get(); // Optional에서 회원 객체를 가져옴

            // 현재 시간을 가져옴
            LocalDateTime startDay = recruitCreateRequestDto.getStartDay();
            LocalDateTime endDay = recruitCreateRequestDto.getEndDay();
            LocalDateTime end = recruitCreateRequestDto.getEnd();
            LocalDateTime currentTime = timeUtil.getCurrentLocalDate();

            // 날짜 유효성 검사
            if (startDay.isBefore(currentTime)) {
                return ResponseEntity.status(400)
                        .body(new FailResponseDto(false, "시작 날짜가 현재 날짜보다 이전입니다.", 400));
            }

            if (endDay.isBefore(currentTime)) {
                return ResponseEntity.status(400)
                        .body(new FailResponseDto(false, "종료 날짜가 현재 날짜보다 이전입니다.", 400));
            }

            if (endDay.isBefore(startDay)) {
                return ResponseEntity.status(400)
                        .body(new FailResponseDto(false, "종료 날짜가 시작 날짜보다 이전입니다.", 400));
            }

            if (end.isBefore(currentTime)) {
                return ResponseEntity.status(400)
                        .body(new FailResponseDto(false, "모집 마감일이 현재 날짜보다 이전입니다.", 400));
            }

            // 새로운 모집 게시글 생성
            Recruit recruit = Recruit.builder()
                    .memberId(member.getId())
                    .kind(recruitCreateRequestDto.getKind())
                    .amount(recruitCreateRequestDto.getAmount())
                    .proceedWay(recruitCreateRequestDto.getProceedWay())
                    .startDay(startDay)
                    .endDay(endDay)
                    .workDay(recruitCreateRequestDto.getWorkDay())
                    .end(end)
                    .contact(recruitCreateRequestDto.getContact())
                    .contactWay(recruitCreateRequestDto.getContactWay())
                    .title(recruitCreateRequestDto.getTitle())
                    .content(recruitCreateRequestDto.getContent())
                    .thumbnail(recruitCreateRequestDto.getThumbnail())
                    .status(true)
                    .viewCount(0)
                    .build();

            // 모집 게시글 저장
            recruitRepository.save(recruit);

            // 모집 포지션 정보가 있을 경우 저장
            if (recruitCreateRequestDto.getPositionList() != null) {
                List<RecruitPosition> recruitPositions = recruitCreateRequestDto.getPositionList().stream()
                        .map(positionDto -> RecruitPosition.builder()
                                .position(positionDto.getPosition())
                                .amount(positionDto.getAmount())
                                .now(positionDto.getNow())
                                .status(positionDto.isStatus())
                                .recruit(recruit)
                                .build())
                        .collect(Collectors.toList());

                recruitPositionRepository.saveAll(recruitPositions);
            }

            // 생성된 모집 게시글의 포지션 정보 조회
            List<RecruitPosition> positions = recruitPositionRepository.findByRecruitId(recruit.getId());

            // DTO로 변환하여 반환할 포지션 리스트 생성
            List<RecruitPositionResponseDto> positionDtoList = positions.stream()
                    .map(position -> RecruitPositionResponseDto.builder()
                            .position(position.getPosition())
                            .amount(position.getAmount())
                            .now(position.getNow())
                            .status(position.isStatus())
                            .build())
                    .collect(Collectors.toList());

            // 생성된 모집 게시글의 정보를 DTO로 변환하여 반환
            RecruitCreateResponseDto create = RecruitCreateResponseDto.builder()
                    .id(recruit.getId())
                    .kind(recruit.getKind())
                    .amount(recruit.getAmount())
                    .proceedWay(recruit.getProceedWay())
                    .startDay(recruit.getStartDay())
                    .endDay(recruit.getEndDay())
                    .workDay(recruit.getWorkDay())
                    .end(recruit.getEnd())
                    .contact(recruit.getContact())
                    .contactWay(recruit.getContactWay())
                    .title(recruit.getTitle())
                    .content(recruit.getContent())
                    .thumbnail(recruit.getThumbnail())
                    .status(recruit.isStatus())
                    .viewCount(recruit.getViewCount())
                    .positionList(positionDtoList)
                    .build();

            // 성공적인 응답 반환
            return ResponseEntity.ok()
                    .body(new SuccessResponseDto(true, "모집 게시글 생성이 완료되었습니다.", create));
        } else {
            // 해당 ID에 대한 회원이 존재하지 않는 경우 실패 응답 반환
            return ResponseEntity.status(400)
                    .body(new FailResponseDto(false, "해당 회원이 존재하지 않습니다.", 400));
        }
    }
}
