package com.creavispace.project.domain.comment.service;

import com.creavispace.project.domain.comment.dto.request.RecruitCommentCreateRequestDto;
import com.creavispace.project.domain.comment.dto.request.RecruitCommentDeleteRequestDto;
import com.creavispace.project.domain.comment.dto.request.RecruitCommentModifyRequestDto;
import com.creavispace.project.domain.comment.dto.response.RecruitCommentCreateResponseDto;
import com.creavispace.project.domain.comment.dto.response.RecruitCommentDeleteResponseDto;
import com.creavispace.project.domain.comment.dto.response.RecruitCommentModifyResponseDto;
import com.creavispace.project.domain.comment.entity.RecruitComment;
import com.creavispace.project.domain.comment.repository.RecruitCommentRepository;
import com.creavispace.project.domain.common.dto.FailResponseDto;
import com.creavispace.project.domain.common.dto.SuccessResponseDto;
import com.creavispace.project.domain.recruit.entity.Recruit;
import com.creavispace.project.domain.recruit.repository.RecruitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.http.HttpResponse;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecruitCommentService {

    private final RecruitCommentRepository recruitCommentRepository;

    @Transactional
    public ResponseEntity createRecruitComment(RecruitCommentCreateRequestDto recruitCommentCreateRequestDto) {
        //jwt를 통해 memeber정보 가져오기
//      Member member = memberRepository.findById(recruitCreateRequestDto.getMemberId()).orElse(null);
        Recruit recruit = RecruitRepository.findById(recruitCommentCreateRequestDto.getRecruitId()).orElse(null);
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
    }

    @Transactional
    public ResponseEntity modifyRecruitComment(RecruitCommentModifyRequestDto recruitCommentModifyRequestDto) {
        //수정요청한 멤버가 글작성 멤버인지 확인
        //jwt토큰으로 멤버 정보가져오기
//        Member member = memberRepository.findById(recruitCreateRequestDto.getMemberId()).orElse(null);
        RecruitComment comment = recruitCommentRepository.findById(recruitCommentModifyRequestDto.getId());

        if(member != comment.getMember()) {
            return ResponseEntity.status(401)
                    .body(new FailResponseDto(false, "댓글을 수정할 수 있는 권한이 없습니다.", 401));
        }

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

    }

    @Transactional
    public ResponseEntity deleteRecruitComment(RecruitCommentDeleteRequestDto recruitCommentDeleteRequestDto) {
        //수정요청한 멤버가 글작성 멤버인지 확인
        //jwt토큰으로 멤버 정보가져오기
//      Member member = memberRepository.findById(recruitCreateRequestDto.getMemberId()).orElse(null);

        RecruitComment comment = recruitCommentRepository.findById(recruitCommentModifyRequestDto.getId());

        if(member != comment.getMember() && !member.getRole().equals("Administrator")) {
            return ResponseEntity.status(401)
                    .body(new FailResponseDto(false, "댓글을 삭제할 수 있는 권한이 없습니다.", 401));
        }

        long id = comment.getId();

        recruitCommentRepository.deleteById(id);

        return ResponseEntity.ok()
                .body(new SuccessResponseDto(true, "댓글 삭제가 완료되었습니다.", id));
    }
}
