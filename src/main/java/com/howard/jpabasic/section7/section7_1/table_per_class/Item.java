package com.howard.jpabasic.section7.section7_1.table_per_class;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Item {

    @Id @GeneratedValue
    private Long id;

    private String name;

    private Integer price;

    /*
     * ==> 이 전략은 DB 설계자와 ORM 전문가 둘 다 추천하지 않는다.
     *
     * 장점
     * 1. 서브 타입을 명확하게 구분해서 처리할 때 효과적이다.
     * 2. NOT NULL 제약조건을 활용할 수 있다.
     *
     * 단점
     * 1. 여러 자식 테이블을 함께 조회할 때 성능이 느림 (UNION SQL 필요)
     * 2. 자식 테이블들을 통합해서 쿼리하기 어려움
     * 3. 변경에 유연하게 대응하기 어렵다.
     *  */

}
