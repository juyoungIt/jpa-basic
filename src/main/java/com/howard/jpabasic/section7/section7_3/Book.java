package com.howard.jpabasic.section7.section7_3;

import jakarta.persistence.Entity;

@Entity
public class Book extends Item {

    private String author;

    private String isbn;

}
