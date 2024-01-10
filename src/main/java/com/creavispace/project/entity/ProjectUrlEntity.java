package com.creavispace.project.entity;

import java.time.LocalDateTime;

import com.creavispace.project.common.ProjectUrlTypeEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "project_url")
public class ProjectUrlEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_id",nullable = false)
    private ProjectEntity project;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "type", nullable = false, columnDefinition = "VARCHAR(20) COMMENT 'WEB OR ANDROID OR IOS'")
    private ProjectUrlTypeEnum type;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "modified_date", nullable = false)
    private LocalDateTime modifiedDate;
}