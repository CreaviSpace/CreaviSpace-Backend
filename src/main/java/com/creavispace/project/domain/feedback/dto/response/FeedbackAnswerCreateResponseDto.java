package com.creavispace.project.domain.feedback.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackAnswerCreateResponseDto {
    private Long id;
    private String question;
    private String answer;
    // enum으로 관리 변경
    private String type;
    private List<ChoiceItemResponseDto> selectedItems;
}