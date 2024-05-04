package com.howard.jpabasic.section6.section6_3.two_way;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Member {

    @Id @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name = "LOCKER_ID")
    private Locker locker;

    private String username;

    /* 연관관계 처리를 수월하게 하기 위한 유틸리티 메서드 */
    public void setLockerRelation(Locker locker) {
        this.locker = locker;
        locker.setMember(this);
    }

}
