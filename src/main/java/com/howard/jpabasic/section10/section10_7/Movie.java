package com.howard.jpabasic.section10.section10_7;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@Entity
public class Movie extends Product {

    private String director;

    private String actor;

}
