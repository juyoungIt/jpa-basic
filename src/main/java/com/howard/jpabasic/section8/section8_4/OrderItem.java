package com.howard.jpabasic.section8.section8_4;

import jakarta.persistence.*;
import lombok.Data;

import static jakarta.persistence.FetchType.*;

@Data
@Entity
public class OrderItem extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "ITEM_ID")
    private Item item;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "ORDER_ID")
    private Order order;

    private Integer orderPrice;

    private Integer count;

}
