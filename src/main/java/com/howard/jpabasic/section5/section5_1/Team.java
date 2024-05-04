package com.howard.jpabasic.section5.section5_1;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Team {

    @Id @GeneratedValue
    @Column(name = "TEAM_ID")
    private Long id;

    private String name;

}
