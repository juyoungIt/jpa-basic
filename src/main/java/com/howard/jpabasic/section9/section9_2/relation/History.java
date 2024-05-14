package com.howard.jpabasic.section9.section9_2.relation;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class History {

    @Id @GeneratedValue
    private Long id;

    private String log;

}
