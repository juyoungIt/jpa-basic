package com.howard.jpabasic.section8.section8_4;

import jakarta.persistence.*;
import lombok.Data;

import static jakarta.persistence.FetchType.*;

@Data
@Entity
public class Delivery extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = LAZY)
    private Order order;

    private String city;

    private String street;

    private String zipcode;

    private DeliveryStatus status;

}
