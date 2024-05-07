package com.howard.jpabasic.section8.section8_2.lazy;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Member {

    @Id @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY) // 지연로딩 설정
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    public void setTeamRelation(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }

}
