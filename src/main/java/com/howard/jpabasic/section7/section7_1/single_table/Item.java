package com.howard.jpabasic.section7.section7_1.single_table;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn // SINGLE_TABLE 전략에서는 자동으로 추가된다
public abstract class Item {

    @Id @GeneratedValue
    private Long id;

    private String name;

    private Integer price;

    /*
     * 장점
     * 1. 조회 시 조인이 필요없기 때문에 조회 성능이 좋은 편이다.
     * 2. 조회 쿼리가 단순하다.
     *
     * 단점
     * 1. 자식 Entity 가 매핑한 column 은 모두 null 을 허용하게 된다.
     * 2. 단일 테이블에 모든 것을 저장하기 때문에 테이블이 커질 수 있다. -> 상황에 따라 조회성능이 오히려 느려질 수 있음
     *  */

}
