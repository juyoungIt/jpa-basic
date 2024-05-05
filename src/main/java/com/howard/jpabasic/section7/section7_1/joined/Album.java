package com.howard.jpabasic.section7.section7_1.joined;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@DiscriminatorValue(value = "ALBUM")
public class Album extends Item {

    private String artist;

}
