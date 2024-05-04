package com.howard.jpabasic.section6.section6_2.two_way;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Team {

    @Id @GeneratedValue
    private Long id;

    private String name;

    @OneToMany
    @JoinColumn(name = "TEAM_ID")
    private List<Member> members = new ArrayList<>();

    public void setMembersRelation(Member member) {
        this.members.add(member);
        member.setTeam(this);
    }
}
