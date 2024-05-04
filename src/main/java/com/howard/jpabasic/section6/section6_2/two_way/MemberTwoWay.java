package com.howard.jpabasic.section6.section6_2.two_way;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class MemberTwoWay {

    @Id @GeneratedValue
    private Long id;

    private String username;

    /* 1:N 양방향이라는 매핑은 공식적으로 존재하지 않는다. */
    /* -> 읽기 전용 필드를 사용해서 양방향처럼 사용하는 전략이라고 이해하면 된다. */
    @ManyToOne
    @JoinColumn(name = "TEAM_ID", insertable = false, updatable = false)
    private TeamTwoWay team;

}
