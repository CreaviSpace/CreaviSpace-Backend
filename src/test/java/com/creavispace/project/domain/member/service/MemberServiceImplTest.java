package com.creavispace.project.domain.member.service;

import static org.junit.jupiter.api.Assertions.*;

import com.creavispace.project.domain.member.dto.response.MemberResponseDto;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MemberServiceImplTest {

    @Autowired
    MemberService memberService;

    @Test
    void findByMemberNicknameOrIdTagContaining() {
        List<MemberResponseDto> data = memberService.findByMemberNicknameOrIdTagContaining("0");
        System.out.println("data = " + data);
    }
}