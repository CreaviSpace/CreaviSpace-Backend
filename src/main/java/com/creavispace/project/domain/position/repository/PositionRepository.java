package com.creavispace.project.domain.position.repository;

import com.creavispace.project.domain.member.entity.Member;
import com.creavispace.project.domain.position.entity.Position;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionRepository extends JpaRepository<Position, Long> {
    Optional<Position> findByMemberId(Member memberId);

    boolean existsByMemberId(Member member);

    boolean existsByPositionId(Long positionId);

    List<Position> findByPosition(String position);

}
