package com.howard.jpabasic.section7.section7_1.table_per_class;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Book extends Item {

    private String author;

    private String isbn;

}
