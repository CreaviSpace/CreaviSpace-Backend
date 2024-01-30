package com.creavispace.project.domain.comment.controller;

import com.creavispace.project.domain.comment.dto.request.RecruitCommentCreateRequestDto;
import com.creavispace.project.domain.comment.dto.request.RecruitCommentDeleteRequestDto;
import com.creavispace.project.domain.comment.dto.request.RecruitCommentModifyRequestDto;
import com.creavispace.project.domain.comment.dto.response.RecruitCommentCreateResponseDto;
import com.creavispace.project.domain.comment.service.RecruitCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recruit/comment")
@RequiredArgsConstructor
public class RecruitCommentController {

    private final RecruitCommentService recruitCommentService;

    @PostMapping("/create")
    public ResponseEntity createRecruitComment(@RequestBody RecruitCommentCreateRequestDto recruitCommentCreateReqeustDto) {
        return recruitCommentService.createRecruitComment(recruitCommentCreateReqeustDto);
    }

    @PutMapping("/modify")
    public ResponseEntity modifyRecruitComment(@RequestBody RecruitCommentModifyRequestDto recruitCommentModifyReqeustDto) {
        return recruitCommentService.modifyRecruitComment(recruitCommentModifyReqeustDto);
    }

    @DeleteMapping("/delete")
    public ResponseEntity deleteRecruitComment(@RequestBody RecruitCommentDeleteRequestDto recruitCommentDeleteRequestDto) {
        return recruitCommentService.deleteRecruitComment(recruitCommentDeleteRequestDto);
    }
}
