package com.howard.jpabasic.section10.section10_8;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class Address {

    private String city;

    private String street;

    private String zipcode;

}
