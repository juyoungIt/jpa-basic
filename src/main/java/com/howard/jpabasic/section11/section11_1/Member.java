package com.howard.jpabasic.section11.section11_1;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Entity
public class Member {

    @Id @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    public void setTeamRelation(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Member member)) return false;
        return Objects.equals(getId(), member.getId())
                && Objects.equals(getName(), member.getName())
                && Objects.equals(getTeam(), member.getTeam());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getTeam());
    }
}
