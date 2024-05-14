package com.howard.jpabasic.section9.section9_2.relation;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

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

}
