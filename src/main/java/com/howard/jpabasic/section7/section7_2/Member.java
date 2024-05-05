package com.howard.jpabasic.section7.section7_2;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Member extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    private String name;

    private Integer age;

    @ManyToOne
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    public void setTeamRelation(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }

}
