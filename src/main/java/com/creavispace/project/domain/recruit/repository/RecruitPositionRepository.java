package com.creavispace.project.domain.recruit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecruitPositionRepository<RecruitPosition> extends JpaRepository<RecruitPosition, Long> {
    List<RecruitPosition> findByRecruitId(Long recruitId);
}
