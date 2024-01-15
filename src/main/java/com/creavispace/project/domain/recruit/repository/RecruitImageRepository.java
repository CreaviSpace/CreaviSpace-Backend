package creavispace.project.domain.recruit.repository;

import creavispace.project.domain.recruit.entity.RecruitImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecruitImageRepository extends JpaRepository<RecruitImage, Long> {
}