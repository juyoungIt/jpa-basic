package com.howard.jpabasic.section9.section9_5.solution_b;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "ADDRESS")
public class AddressEntity {

    @Id @GeneratedValue
    private Long id;

    private Address address;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    /* 연관관계 업데이트를 용이하게 하기 위한 유틸리티 메서드 */
    public void setMemberRelation(Member member) {
        this.member = member;
        member.getAddressHistory().add(this);
    }

    @Override
    public String toString() {
        return "AddressEntity{" +
                "id=" + id +
                ", address=" + address +
                '}';
    }
}
