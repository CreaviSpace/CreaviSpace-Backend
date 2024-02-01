package com.creavispace.project.domain.comment.service;

import com.creavispace.project.domain.comment.dto.request.RecruitCommentCreateRequestDto;
import com.creavispace.project.domain.comment.dto.request.RecruitCommentDeleteRequestDto;
import com.creavispace.project.domain.comment.dto.request.RecruitCommentModifyRequestDto;
import com.creavispace.project.domain.comment.dto.response.RecruitCommentCreateResponseDto;
import com.creavispace.project.domain.comment.dto.response.RecruitCommentModifyResponseDto;
import com.creavispace.project.domain.comment.entity.RecruitComment;
import com.creavispace.project.domain.comment.repository.RecruitCommentRepository;
import com.creavispace.project.domain.common.dto.FailResponseDto;
import com.creavispace.project.domain.common.dto.SuccessResponseDto;
import com.creavispace.project.domain.member.entity.Member;
import com.creavispace.project.domain.member.repository.MemberRepository;
import com.creavispace.project.domain.recruit.entity.Recruit;
import com.creavispace.project.domain.recruit.repository.RecruitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.http.HttpResponse;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecruitCommentService {

    private final RecruitCommentRepository recruitCommentRepository;
    private final MemberRepository memberRepository;
    private final RecruitRepository recruitRepository;

    @Transactional
    public ResponseEntity createRecruitComment(RecruitCommentCreateRequestDto recruitCommentCreateRequestDto) {
        //jwt를 통해 memeber정보 가져오기
        //Member member = memberRepository.findById(recruitCommentCreateRequestDto.getMemberId()).orElse(null);
        long memberId = 1L;
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        Optional<Recruit> optionalRecruit = recruitRepository.findById(recruitCommentCreateRequestDto.getRecruitId());

        if (optionalMember.isPresent() && optionalRecruit.isPresent()) {
            Member member = optionalMember.get();
            Recruit recruit = optionalRecruit.get();

            RecruitComment recruitComment = RecruitComment.builder()
                    .content(recruitCommentCreateRequestDto.getContent())
                    .recruit(recruit)
                    .member(member)
                    .build();

            recruitCommentRepository.save(recruitComment);

            RecruitCommentCreateResponseDto create = RecruitCommentCreateResponseDto.builder()
                    .id(create.getId())
                    .recruitId(create.getRecruitId())
                    .memberId(create.getMemberId())
                    .modifiedDate(create.getModifiedDate())
                    .content(create.getContent())
                    .build();

            return ResponseEntity.ok()
                    .body(new SuccessResponseDto(true, "댓글 작성이 완료되었습니다.", create));
        } else {
            return ResponseEntity.status(400)
                    .body(new FailResponseDto(false, "해당 회원 또는 게시글이 존재하지 않습니다.", 400));
        }
    }

    @Transactional
    public ResponseEntity modifyRecruitComment(RecruitCommentModifyRequestDto recruitCommentModifyRequestDto) {
        //수정요청한 멤버가 글작성 멤버인지 확인
        //jwt토큰으로 멤버 정보가져오기
//        Member member =
        long memberId = 1L;
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        Optional<RecruitComment> optionalComment = recruitCommentRepository.findById(recruitCommentModifyRequestDto.getId());

        if (optionalMember.isPresent() && optionalComment.isPresent()) {
            Member member = optionalMember.get();
            RecruitComment comment = optionalComment.get();

            if (member.equals(comment.getMember())) {
                comment = RecruitComment.builder()
                        .id(comment.getId())
                        .content(recruitCommentModifyRequestDto.getContent())
                        .recruit(comment.getRecruit())
                        .member(comment.getMember())
                        .build();

                recruitCommentRepository.save(comment);

                RecruitCommentModifyResponseDto create = new RecruitCommentModifyResponseDto.builder()
                        .id(comment.getId())
                        .recruitId(comment.getRecruit().getId())
                        .memberId(comment.getMember().getId())
                        .modifiedDate(comment.getModifiedDate())
                        .content(comment.getContent())
                        .build();

                return ResponseEntity.ok()
                        .body(new SuccessResponseDto(true, "댓글 수정이 완료되었습니다.", create));
            } else {
                return ResponseEntity.status(401)
                        .body(new FailResponseDto(false, "댓글을 수정할 수 있는 권한이 없습니다.", 401));
            }
        } else {
            return ResponseEntity.status(400)
                    .body(new FailResponseDto(false, "해당 회원 또는 댓글이 존재하지 않습니다.", 400));
        }
    }

    @Transactional
    public ResponseEntity deleteRecruitComment(RecruitCommentDeleteRequestDto recruitCommentDeleteRequestDto) {
        //수정요청한 멤버가 글작성 멤버인지 확인
        //jwt토큰으로 멤버 정보가져오기
//        Member member = memberRepository.findById(recruitCommentCreateRequestDto.getMemberId()).orElse(null);

        long memberId = 1L;
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        Optional<RecruitComment> optionalComment = recruitCommentRepository.findById(recruitCommentDeleteRequestDto.getId());

        if (optionalMember.isPresent() && optionalComment.isPresent()) {
            Member member = optionalMember.get();
            RecruitComment comment = optionalComment.get();

            if (member.equals(comment.getMember()) || "Administrator".equals(member.getRole())) {
                long id = comment.getId();

                recruitCommentRepository.deleteById(id);

                return ResponseEntity.ok()
                        .body(new SuccessResponseDto(true, "댓글 삭제가 완료되었습니다.", id));
            } else {
                return ResponseEntity.status(401)
                        .body(new FailResponseDto(false, "댓글을 삭제할 수 있는 권한이 없습니다.", 401));
            }
        } else {
            return ResponseEntity.status(400)
                    .body(new FailResponseDto(false, "해당 회원 또는 댓글이 존재하지 않습니다.", 400));
        }
    }
}
