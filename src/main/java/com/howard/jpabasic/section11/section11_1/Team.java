package com.howard.jpabasic.section11.section11_1;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
public class Team {

    @Id @GeneratedValue
    private Long id;

    private String name;

    @OneToMany(mappedBy = "team")
    private List<Member> members = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Team team)) return false;
        return Objects.equals(getId(), team.getId())
                && Objects.equals(getName(), team.getName())
                && Objects.equals(getMembers(), team.getMembers());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getMembers());
    }
}
