package com.howard.jpabasic.section9.section9_6;

import jakarta.persistence.Entity;

@Entity
public class Book extends Item {

    private String author;

    private String isbn;

}
