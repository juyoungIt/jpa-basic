package com.howard.jpabasic.section9.section9_6;

import jakarta.persistence.Entity;

@Entity
public class Movie extends Item {

    private String director;

    private String actor;

}
