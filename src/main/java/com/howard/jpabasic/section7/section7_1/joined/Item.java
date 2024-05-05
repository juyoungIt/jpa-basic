package com.howard.jpabasic.section7.section7_1.joined;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "DTYPE")
public abstract class Item {

    @Id @GeneratedValue
    private Long id;

    private String name;

    private Integer price;

    /*
    * 장점
    * 1. 테이블 정규화를 이상적으로 수행한 모델이다.
    * 2. 외래 키 참조 무결성 제약조건을 활용할 수 있다.
    * 3. 저장공간의 효율적 사용이 가능하다. -> 정규화를 이상적으로 했기 때문
    *
    * 단점
    * 1. 조회 시 조인을 많이 사용하게 되어 성능저하가 발생할 수 있다.
    * 2. 조회 쿼리가 복잡하다.
    * 3. Table 이 나눠져 있어 Insert SQL 이 2회 요청된다.
    *  */

}
