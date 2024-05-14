package com.howard.jpabasic.section9.section9_4.use_equals;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EntityScan(basePackages = {"com.howard.jpabasic.section9.section9_4.use_equals"})
public class Section9Test {

    @PersistenceUnit
    private EntityManagerFactory emf;
    private EntityManager em;
    private EntityTransaction etx;

    @BeforeEach
    public void init() {
        /* 각 테스트별로 EntityManager 및 Transaction 초기화 */
        em = emf.createEntityManager();
        etx = em.getTransaction();
    }

    @Test
    @DisplayName("동일성(identity) 비교 - 인스턴스의 참조값을 비교, == 사용")
    public void identityComparisonTest() {
        try {
            etx.begin();

            /* Member Entity 생성 및 영속화 */
            Member memberA = new Member();
            memberA.setId(1L);
            memberA.setName("ryan");
            memberA.setWorkPeriod(new Period(
                    LocalDateTime.of(2024, 2, 1, 0, 0, 0),
                    LocalDateTime.now()
            ));
            memberA.setHomeAddress(new Address("homeCity", "homeStreet", "homeZipcode"));

            Member memberB = memberA; // memberA와 memberB가 서로 같은 주소값을 가지게 된다.

            /* MemberA와 속성값은 동일하게 세팅 */
            Member memberC = new Member();
            memberC.setId(1L);
            memberC.setName("ryan");
            memberC.setWorkPeriod(new Period(
                    LocalDateTime.of(2024, 2, 1, 0, 0, 0),
                    LocalDateTime.now()
            ));
            memberC.setHomeAddress(new Address("homeCity", "homeStreet", "homeZipcode"));

            System.out.println("memberA == memberB : " + (memberA == memberB)); // 서로 같은 인스턴스 이므로 True
            System.out.println("memberA == memberC : " + (memberA == memberC)); // 서로 다른 인스턴스 이므로 False
            /* 동일성을 비교하는 코드, '==' 연사자를 사용하여 비교하는 것과 같다 */
            assertThat(memberA).isSameAs(memberB);
            assertThat(memberA).isNotSameAs(memberC);

            etx.commit();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

    @Test
    @DisplayName("동등성(equivalence) 비교 - 인스턴스의 값을 비교, equals() 사용 -> 하지만 equals() 를 따로 구현하지 않은 경우")
    public void equivalenceComparisonTest() {
        try {
            etx.begin();

            /* Member Entity 생성 및 영속화 */
            Member memberA = new Member();
            memberA.setId(1L);
            memberA.setName("ryan");
            memberA.setWorkPeriod(new Period(
                    LocalDateTime.of(2024, 2, 1, 0, 0, 0),
                    LocalDateTime.of(2025, 2, 1, 0, 0, 0)
            ));
            memberA.setHomeAddress(new Address("homeCity", "homeStreet", "homeZipcode"));

            Member memberB = memberA; // memberA와 memberB가 서로 같은 주소값을 가지게 된다.

            /* MemberA와 속성값은 동일하게 세팅 */
            Member memberC = new Member();
            memberC.setId(1L);
            memberC.setName("ryan");
            memberC.setWorkPeriod(new Period(
                    LocalDateTime.of(2024, 2, 1, 0, 0, 0),
                    LocalDateTime.of(2025, 2, 1, 0, 0, 0)
            ));
            memberC.setHomeAddress(new Address("homeCity", "homeStreet", "homeZipcode"));

            System.out.println("memberA.equals(memberB) : " + (memberA.equals(memberB))); // 같은 인스턴스 이므로 True
            System.out.println("memberA.equals(memberC) : " + (memberA.equals(memberC))); // 같은 값이므로 True
            /* 동등성을 비교하는 코드, equals() 를 사용하여 비교하는 것과 같다 */
            assertThat(memberA).isEqualTo(memberB);
            assertThat(memberA).isEqualTo(memberC);

            etx.commit();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }
}
