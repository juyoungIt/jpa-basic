package com.howard.jpabasic.section8.section8_4;

import jakarta.persistence.Entity;

@Entity
public class Movie extends Item {

    private String director;

    private String actor;

}
