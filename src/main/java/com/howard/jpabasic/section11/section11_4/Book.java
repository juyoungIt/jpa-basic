package com.howard.jpabasic.section11.section11_4;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Book extends Item {

    private String author;

    private String isbn;

}
