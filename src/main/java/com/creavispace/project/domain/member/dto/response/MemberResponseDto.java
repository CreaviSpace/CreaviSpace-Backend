package com.creavispace.project.domain.member.dto.response;

import java.util.List;
import lombok.Data;
/**
 * 사용자가 조회 할 수 있는 데이터
 * 이메일
 * 비밀번호
 * 닉네임
 * 자기소개
 * 직무
 * 관심 기술*/
@Data
public class MemberResponseDto {
    private String memberEmail;
    private String memberPassword;
    private String memberNickname;
    private String memberPosition;
    private List<String> memberInterested;
    private String introduce;
}