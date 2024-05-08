package com.howard.jpabasic.section8.section8_3;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Parent {

    @Id @GeneratedValue
    private Long id;

    private String name;

    /* 해당 컬렉션 내에 있는 Entity 들도 모두 persist 하도록 설정함 */
    /* 1. 소유자가 하나일 때 */
    /* 2. 둘의 라이프 사이클이 거의 동일할 때 */
    /* -> 그 때 이를 사용할 수 있으니 잘 기억하도록 하자 */
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Children> childList = new ArrayList<>();

}
