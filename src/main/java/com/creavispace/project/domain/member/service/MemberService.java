package com.creavispace.project.domain.member.service;

import com.creavispace.project.domain.member.dto.request.MemberSaveRequestDto;
import com.creavispace.project.domain.member.dto.response.MemberResponseDto;
import com.creavispace.project.domain.member.entity.Member;
import com.creavispace.project.domain.member.dto.request.MemberUpdateRequestDto;
import java.util.List;
import java.util.Optional;

public interface MemberService {
    MemberSaveRequestDto save(MemberSaveRequestDto member);

    void update(Long memberId, MemberUpdateRequestDto updateParam);

    Optional<Member> findByEmail(String email);

    MemberResponseDto findById(Long memberId);

    boolean emailExsists(String memberEmail);

    List<Member> findAllMembers();
}
