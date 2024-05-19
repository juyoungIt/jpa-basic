package com.howard.jpabasic.section10.section10_7;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "ORDERS")
public class Order {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    private int orderAmount;

    @Embedded
    private Address address;

}
