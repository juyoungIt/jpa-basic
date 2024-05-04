package com.howard.jpabasic.section5.section5_2and3;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Member {

    @Id
    @GeneratedValue
    private Long id;

    private String username;

    private Integer age;

    @ManyToOne
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    public void setTeamRelation(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }

}
