package com.howard.jpabasic.section4.section4_4;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Member")
@TableGenerator(
        name = "MEMBER_SEQ_GENERATOR",
        table = "MY_SEQUENCES",
        pkColumnValue = "MEMBER_SEQ",
        allocationSize = 1
)
public class MemberKeyByTable {

    @Id
    @GeneratedValue(
            strategy = GenerationType.TABLE,
            generator = "MEMBER_SEQ_GENERATOR"
    )
    private Long id;

    private String username;

    private Integer age;

}
