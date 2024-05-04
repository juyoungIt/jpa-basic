package com.howard.jpabasic.section4.section4_4;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "member")
/* Table 별로 구분된 Sequence Object 를 사용하고 싶은 경우 */
@SequenceGenerator(
        name = "MEMBER_SEQ_GENERATOR",
        sequenceName = "MEMBER_SEQ" // 매핑할 데이터베이스 시퀀스 이름
)
public class MemberKeyBySeparatedSequence {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "MEMBER_SEQ_GENERATOR"
    )
    private Long id;

    private String username;

    private Integer age;

}
