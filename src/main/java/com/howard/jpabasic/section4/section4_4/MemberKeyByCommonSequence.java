package com.howard.jpabasic.section4.section4_4;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "member")
public class MemberKeyByCommonSequence {

    @Id
    /* 모든 Table 들이 공통의 Sequence Object를 공유하게 됨 */
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String username;

    private Integer age;

}
