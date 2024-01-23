package com.creavispace.project.member;

import static org.assertj.core.api.Assertions.assertThat;

import com.creavispace.project.domain.member.dto.request.MemberUpdateRequestDto;
import com.creavispace.project.domain.member.dto.request.MemberSaveRequestDto;
import com.creavispace.project.domain.member.entity.Member;
import com.creavispace.project.domain.member.repository.MemberRepository;
import com.creavispace.project.domain.position.repository.PositionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PositionRepository positionRepository;

    @Test
    @Commit
    void testSave() {
        MemberSaveRequestDto dto = MemberSaveRequestDto.builder()
                .memberEmail("1r2344ㅁ4@naver.com")
                .memberPassword("12455")
                .memberNickname("rrla4ㅁr22dud1")
                .build();

        Member member = new Member(dto);
        memberRepository.save(member);
        System.out.println(memberRepository.findById(member.getId()));
        assertThat(member).isEqualTo(memberRepository.findById(member.getId()).orElseThrow());
    }

    @Test
    void testRead() {
        Member member = memberRepository.findById(1L).orElseThrow();
        System.out.println(member.getMemberEmail());
    }

    @Test
    void testEdit() {
        Member beforeEdit = memberRepository.findById(1L).orElseThrow();
        System.out.println(beforeEdit);
        MemberUpdateRequestDto dto = new MemberUpdateRequestDto();
        dto.setMemberPassword("바뀜");
        dto.setMemberNickname("999999999999");
        beforeEdit.setMemberPassword(dto.getMemberPassword());
        beforeEdit.setMemberNickname(dto.getMemberNickname());
        beforeEdit.setMemberIntroduce(dto.getIntroduce());
        memberRepository.save(beforeEdit);
        System.out.println(memberRepository.findById(1L));
    }
}