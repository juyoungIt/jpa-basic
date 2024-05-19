package com.howard.jpabasic.section10.section10_5;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Member {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    private String username;

    private int age;

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    public void setTeamRelation(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }

}
