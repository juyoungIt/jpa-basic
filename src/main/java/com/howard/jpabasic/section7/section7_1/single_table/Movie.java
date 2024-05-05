package com.howard.jpabasic.section7.section7_1.single_table;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@DiscriminatorValue(value = "MOVIE")
public class Movie extends Item {

    private String director;

    private String actor;

}
