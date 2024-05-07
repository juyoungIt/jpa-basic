package com.howard.jpabasic.section8.section8_1;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Member {

    @Id @GeneratedValue
    private Long id;

    private String username;

    private Integer age;

    private LocalDateTime createAt;

    private LocalDateTime lastModifiedAt;

}
