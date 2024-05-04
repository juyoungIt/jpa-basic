package com.howard.jpabasic.section4.section4_4;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "Member")
public class MemberKeyByUser {

    @Id
    private String id;

    private String username;

    private Integer age;

}
