package com.howard.jpabasic.section8.section8_2.eager;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Member {

    @Id @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.EAGER) // 즉시로딩 설정
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    public void setTeamRelation(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }

}
