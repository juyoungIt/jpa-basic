package com.howard.jpabasic.section6.section6_4.one_way;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Product {

    @Id @GeneratedValue
    private Long id;

    private String name;

}
