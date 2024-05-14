package com.howard.jpabasic.section9.section9_5.solution_a;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    /* 컬렉션 값 타입이 아닌 Entity 로 접근하는 방법 */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "MEMBER_ID")
    private List<AddressEntity> addressHistory = new ArrayList<>();

}
