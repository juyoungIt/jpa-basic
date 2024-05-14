package com.howard.jpabasic.section9.section9_6;

import jakarta.persistence.MappedSuperclass;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@MappedSuperclass
public class BaseEntity {

    private LocalDateTime createAt;

    private LocalDateTime lastModifiedAt;

}
