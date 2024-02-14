//package com.creavispace.project.domain.bookmark.service;
//
//import com.creavispace.project.domain.bookmark.dto.request.RecruitBookmarkRequestDto;
//import com.creavispace.project.domain.bookmark.entity.RecruitBookmark;
//import com.creavispace.project.domain.bookmark.repository.RecruitBookmarkRepository;
//import com.creavispace.project.domain.member.repository.MemberRepository;
//import com.creavispace.project.domain.recruit.entity.Recruit;
//import com.creavispace.project.domain.recruit.repository.RecruitRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//@RequiredArgsConstructor
//@Transactional(readOnly = true)
//public class RecruitBookmarkService {
//
//    private final RecruitBookmarkRepository recruitBookmarkRepository;
//
//    private final RecruitRepository recruitRepository;
//
//    @Transactional
//    public ResponseEntity recruitBookmark(RecruitBookmarkRequestDto recruitBookmarkRequestDto) {
//        //
////        Member member=;
//        Recruit recruit = recruitRepository.findById(recruitBookmarkRequestDto.getRecruitId());
//
//        RecruitBookmark recruitBookmark = recruitBookmarkRepository.findByRecruitAndMember();
//    }
//}
