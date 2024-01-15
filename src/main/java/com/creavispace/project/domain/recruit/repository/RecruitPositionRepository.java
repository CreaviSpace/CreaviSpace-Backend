package creavispace.project.domain.recruit.repository;

import creavispace.project.domain.recruit.entity.Recruit;
import creavispace.project.domain.recruit.entity.RecruitPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecruitPositionRepository extends JpaRepository<RecruitPosition, Long> {
}
