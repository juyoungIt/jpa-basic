package com.howard.jpabasic.section9.section9_4.use_equals;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Objects;

@Data
@Entity
public class Member {

    @Id @GeneratedValue
    private Long id;

    private String name;

    @Embedded // 값을 사용하는 곳에 표시
    private Period workPeriod;

    @Embedded // 값을 사용하는 곳에 표시
    private Address homeAddress;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Member member)) return false;
        return Objects.equals(getId(), member.getId())
                && Objects.equals(getName(), member.getName())
                && Objects.equals(getWorkPeriod(), member.getWorkPeriod())
                && Objects.equals(getHomeAddress(), member.getHomeAddress());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getWorkPeriod(), getHomeAddress());
    }
}
