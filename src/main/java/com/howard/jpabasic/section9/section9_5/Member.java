package com.howard.jpabasic.section9.section9_5;

import jakarta.persistence.*;
import lombok.Data;

import java.util.*;

@Data
@Entity
public class Member {

    @Id @GeneratedValue
    private Long id;

    private String name;

    @Embedded
    private Address homeAddress;

    @ElementCollection
    @CollectionTable(name = "FAVORITE_FOOD", joinColumns = @JoinColumn(name = "MEMBER_ID"))
    @Column(name = "FOOD_NAME") // 기본형 타입인 경우 예외적으로 다음과 같이 column 정보를 매핑할 수 있음
    private Set<String> favoriteFoods = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "ADDRESS_HISTORY", joinColumns = @JoinColumn(name = "MEMBER_ID"))
    private List<Address> addressHistory = new ArrayList<>();

    /*
     * 값 타입 컬렉션
     * 1. 값 타입을 하나 이상 저장할 때 사용
     * 2. @ElementCollection, @CollectionTable
     * 3. (관계형)DB는 그 구조상 컬렉션을 같은 테이블 상에 저장할 수 없음
     * 4. 따라서 -> 컬렉션 자료구조를 DB에 저장하기 위한 별도의 Table 이 필요
     * */

    /*
     * (중요!) 값 타입 컬렉션의 제약사항
     * 1. 값 타입의 경우 Entity 와 달리 식별자의 개념이 없다.
     * 2. 즉, 값을 변경하게 되면 추적이 어렵다.
     * 3. 값 타입 컬렉션에 변경사항이 발생하면 주인 Entity 와 연관된 모든 데이터를 삭제하고, 값 타입 컬렉션에 있는 값을 모두 다시 저장한다.
     * 4. 그래서 값 타입 컬렉션을 매핑하는 테이블은 모든 컬럼을 묶어서 기본키를 구성하게 된다. (null 입력X, 중복 저장X)
     * 5. 그렇기 때문에 실무에서는 상황에 따라 값 타입 컬렉션 대신에 일대다 관계를 고려
     * 6. 일대다 관계를 위한 엔티티를 만들고, 여기에 값 타입을 사용하도록 변경
     * 7. 영속성 전이 (CASCADE) + 고아 객체 제거를 이용해서 값 타입 컬렉션처럼 사용
     * -> 그렇기 때문에 값 타입 컬렉션은 아주 간단한 상황에서만 적용하여 사용하는 것을 권장한다.
     *
     * -> 따라서, 엔티티와 값 타입을 혼동해서 엔티티를 값 타입으로 만드는 실수를 범해서는 안된다.
     * -> 식별자가 필요하고 지속적으로 값을 추적, 변경 해야한다면 그것은 값이 아닌 엔티티가 되어야함을 기억하자
     * */

}
