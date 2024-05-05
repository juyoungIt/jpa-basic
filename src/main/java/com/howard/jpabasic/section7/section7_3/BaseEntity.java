package com.howard.jpabasic.section7.section7_3;

import jakarta.persistence.MappedSuperclass;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@MappedSuperclass
public class BaseEntity {

    private LocalDateTime createAt;

    private LocalDateTime lastModifiedAt;

}
