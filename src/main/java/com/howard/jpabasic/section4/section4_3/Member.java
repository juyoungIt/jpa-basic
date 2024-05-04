package com.howard.jpabasic.section4.section4_3;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Member {

    @Id @GeneratedValue
    public Long id;

    @Column(name = "name")
    private String username;

    private Integer age;

    @Enumerated(EnumType.STRING) // EnumType.ORDINAL 의 사용은 지양하자
    private RoleType roleType;

    private LocalDateTime createDate;

    private LocalDateTime modifiedDate;

    @Lob
    private String description;

    @Transient // 메모리 상에서만 임시로 유지하고 싶은 경우 사용
    private String hiddenType;

}
