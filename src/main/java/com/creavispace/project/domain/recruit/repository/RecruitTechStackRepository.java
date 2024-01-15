package creavispace.project.domain.recruit.repository;

import creavispace.project.domain.recruit.entity.RecruitTechStack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecruitTechStackRepository extends JpaRepository<RecruitTechStack, Long> {
}
