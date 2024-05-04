package com.howard.jpabasic.section6.section6_1.one_way;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Member {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    private String username;

}
