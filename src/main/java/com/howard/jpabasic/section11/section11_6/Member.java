package com.howard.jpabasic.section11.section11_6;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@NamedQuery(
        name = "Member.findByName",
        query = "select m from com.howard.jpabasic.section11.section11_6.Member m where m.name = :name"
)
@Entity
public class Member {

    @Id @GeneratedValue
    private Long id;

    private String name;

}
