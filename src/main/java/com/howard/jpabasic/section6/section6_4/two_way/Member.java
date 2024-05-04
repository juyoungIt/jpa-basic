package com.howard.jpabasic.section6.section6_4.two_way;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Member {

    @Id @GeneratedValue
    private Long id;

    private String name;

    @ManyToMany
    @JoinTable(name = "MEMBER_PRODUCT") // 여기서 지정한 값이 중간 테이블의 이름이 된다
    private List<Product> products = new ArrayList<>();

    public void setProductRelation(Product product) {
        this.products.add(product);
        product.getMembers().add(this);
    }

}
