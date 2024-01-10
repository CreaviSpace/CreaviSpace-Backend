package com.creavispace.project.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.creavispace.project.common.ProjectKindEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "project")
public class ProjectEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_field")
    private String projectField;

    @Column(name = "title", length = 200, nullable = false)
    private String title;

    @Column(name = "project_content", nullable = false, columnDefinition = "TEXT")
    private String projectContent;

    @Column(name = "banner_content", nullable = false, columnDefinition = "TEXT")
    private String bannerContent;

    @Column(name = "kind" , nullable = false, columnDefinition = "VARCHAR(20) COMMENT 'PERSONAL OR TEAM'")
    private ProjectKindEnum kind;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity member;

    @Column(name = "view_count", nullable = false, columnDefinition = "int default 0")
    private int viewCount;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "modified_date", nullable = false)
    private LocalDateTime modifiedDate;

    @OneToMany(mappedBy = "project")
    private List<ProjectImageEntity> imageList;

    @OneToMany(mappedBy = "project")
    private List<ProjectUrlEntity> urlList;

    @OneToMany(mappedBy = "project")
    private List<ProjectReplyEntity> replyList;
}
