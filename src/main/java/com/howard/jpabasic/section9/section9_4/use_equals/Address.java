package com.howard.jpabasic.section9.section9_4.use_equals;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/* 다음과 같이 임베디드 타입 사용 시 장점
 * 1. 재사용
 * 2. 높은 응집도
 * 3. 해당 값 타입만 사용하는 의미있는 메서드를 만들어서 사용할 수 있다.
 * 4. 해당 값을 소유한 엔티티에 생명주기를 의존하게 됨 (모든 값 타입이 그렇다)
 * */

/*
 * 임베디드 타입과 테이블 매핑
 * 1. 임베디드 타입은 엔티티의 값일 뿐이다.
 * 2. 임베디드 타입을 사용하기 전과 후에 매핑하는 테이블은 서로 같다.
 * 3. 객체와 테이블을 아주 세밀(find-grained) 매핑하는 것이 가능해진다.
 * 4. 잘 설계한 ORM 애플리케이션의 경우, 매핑한 테이블의 수보다 클래스의 수가 더 많다.
 * */

@Data
@NoArgsConstructor // 기본생성자를 가지고 있어야 함
@AllArgsConstructor
@Embeddable        // 값을 정의하는 곳에 표시
public class Address {

    private String city;

    private String street;

    private String zipcode;

}
