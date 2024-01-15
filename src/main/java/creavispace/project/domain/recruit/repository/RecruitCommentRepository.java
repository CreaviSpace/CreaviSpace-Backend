package creavispace.project.domain.recruit.repository;

import creavispace.project.domain.recruit.entity.RecruitComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecruitCommentRepository extends JpaRepository<RecruitComment, Long> {
}
