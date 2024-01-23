package com.creavispace.project.domain.position.entity;

import com.creavispace.project.domain.member.entity.Member;
import com.creavispace.project.domain.position.dto.request.PositionRequestDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "position")
@NoArgsConstructor
public class Position {

    @Id
    @Column(name = "position_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long positionId;

    @ManyToOne
    @JoinColumn(name = "member_id")
    Member memberId;

    String position;

    public Position(PositionRequestDto dto) {
        this.memberId = dto.getMemberId();
        this.position = dto.getPosition();
    }
}
