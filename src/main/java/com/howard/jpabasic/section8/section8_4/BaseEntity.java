package com.howard.jpabasic.section8.section8_4;

import jakarta.persistence.MappedSuperclass;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@MappedSuperclass
public class BaseEntity {

    private LocalDateTime createAt;

    private LocalDateTime lastModifiedAt;

}
