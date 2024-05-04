package com.howard.jpabasic.section6.section6_1.two_way;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Member {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    private String username;

    /* 양방향 연관관계 세팅을 유용하게 처리하기 위한 유틸리티 메서드 */
    public void setTeamRelation(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }

}
