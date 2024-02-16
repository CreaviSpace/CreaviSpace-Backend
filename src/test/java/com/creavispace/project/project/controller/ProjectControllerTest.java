package com.creavispace.project.project.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.creavispace.project.domain.common.dto.SuccessResponseDto;
import com.creavispace.project.domain.project.controller.ProjectController;
import com.creavispace.project.domain.project.dto.request.ProjectCreateRequestDto;
import com.creavispace.project.domain.project.dto.request.ProjectMemberCreateRequestDto;
import com.creavispace.project.domain.project.dto.request.ProjectTechStackCreateRequestDto;
import com.creavispace.project.domain.project.dto.response.ProjectCreateResponseDto;
import com.creavispace.project.domain.project.dto.response.ProjectMemberResponseDto;
import com.creavispace.project.domain.project.dto.response.ProjectTechStackResponseDto;
import com.creavispace.project.domain.project.entity.ProjectKind;
import com.creavispace.project.domain.project.service.ProjectService;
import com.google.gson.Gson;

@ExtendWith(MockitoExtension.class)
public class ProjectControllerTest {
    
    @InjectMocks
    private ProjectController projectController;

    @Mock
    private ProjectService projectService;

    private MockMvc mockMvc;

    @BeforeEach
    public void init(){
        mockMvc = MockMvcBuilders.standaloneSetup(projectController).build();
    }

    @DisplayName("프로젝트 생성 완료")
    @Test
    void createProjectSuccess() throws Exception {
        // given
        ProjectCreateRequestDto request = ProjectCreateRequestDto.builder()
            .kind("TEAM")
            .field(null)
            .title("creavi")
            .content("creavi_content")
            .link("")
            .thumbnail(null)
            .bannerContent("creavi_bannerContent")
            .projectMemberList(List.of(
                new ProjectMemberCreateRequestDto(1L, "backend"),
                new ProjectMemberCreateRequestDto(2L, "backend")
                ))
            .projectTechStackList(List.of(
                new ProjectTechStackCreateRequestDto(1L),
                new ProjectTechStackCreateRequestDto(2L)
            ))
            .build();

        ProjectCreateResponseDto responseDto = ProjectCreateResponseDto.builder()
            .id(1L)
            .kind(ProjectKind.TEAM)
            .field("")
            .title("creavi")
            .content("creavi_content")
            .link("")
            .thumbnail("")
            .bannerContent("creavi_bannerContent")
            .viewCount(0)
            .createdDate(null)
            .modifiedDate(null)
            .projectMemberList(List.of(
                new ProjectMemberResponseDto(1L,1L,"test1@gmail.com","http://test1", "backend"),
                new ProjectMemberResponseDto(2L,2L,"test2@gmail.com","http://test2", "backend")
            ))
            .projectTechStackList(List.of(
                new ProjectTechStackResponseDto(1L,1L,"java","http://java"),
                new ProjectTechStackResponseDto(2L,2L,"react","http://react")
            ))
            .build();

        ResponseEntity<SuccessResponseDto> responseEntity = ResponseEntity.ok().body(new SuccessResponseDto(true, "프로젝트 게시글 생성이 완료 되었습니다.", responseDto));

        doReturn(responseEntity).when(projectService)
            .createProject(any(ProjectCreateRequestDto.class));
        
        // when
        ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.post("/project/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(request))
        );

        // then
        resultActions.andExpect(status().isOk())
                 .andExpect(jsonPath("$.success").value(true))
                 .andExpect(jsonPath("$.message").value("프로젝트 게시글 생성이 완료 되었습니다."));
    }
    
}
