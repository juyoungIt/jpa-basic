package com.howard.jpabasic.section11.section11_4;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Entity
public abstract class Item {

    @Id @GeneratedValue
    private Long id;

    private String name;

    private int price;

}
