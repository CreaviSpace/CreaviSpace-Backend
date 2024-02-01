package com.creavispace.project.domain.bookmark.repository;

import com.creavispace.project.domain.bookmark.entity.RecruitBookmark;
import com.creavispace.project.domain.recruit.entity.Recruit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecruitBookmarkRepository extends JpaRepository<RecruitBookmark, Long> {
    List<RecruitBookmark> findByMember(Member member);

    List<RecruitBookmark> findByRecruit(Recruit recruit);

    List<RecruitBookmark> findByRecruitAndMember(Recruit recruit, Member member);
}
