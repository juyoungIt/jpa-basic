package com.howard.jpabasic.section7.section7_3;

import jakarta.persistence.Entity;

@Entity
public class Movie extends Item {

    private String director;

    private String actor;

}
