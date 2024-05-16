package com.howard.jpabasic.section10.section10_1;

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

}
