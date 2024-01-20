package com.creavispace.project.domain.comment.repository;

import com.creavispace.project.domain.recruit.entity.Recruit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecruitCommentRepository<RecruitComment> extends JpaRepository<RecruitComment, Long> {
    List<RecruitComment> findByRecruit(Recruit recruit);
}
