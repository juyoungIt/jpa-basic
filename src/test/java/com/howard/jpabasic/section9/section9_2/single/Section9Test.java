package com.howard.jpabasic.section9.section9_2.single;

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

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@EntityScan(basePackages = {"com.howard.jpabasic.section9.section9_2.single"})
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
    @DisplayName("@Embeddable, @Embedded - 임베디드 타입을 사용하더라도 사용 전과 후의 매핑되는 테이블 구조는 동일하다")
    public void embeddedTypeTest() {
        try {
            etx.begin();

            /* Member Entity 생성 및 영속화 */
            Member member = new Member();
            member.setName("ryan");
            member.setWorkPeriod(new Period(
                    LocalDateTime.of(2023, 2, 1, 8, 0, 0),
                    LocalDateTime.now()));
            member.setHomeAddress(new Address("streetA", "cityA", "zipcodeA"));
            em.persist(member);

            /* Select query 를 확인하기 위한 목적으로 작성한 코드 */
            em.flush();
            em.clear();

            /* 영속화한 Member Entity 조회 */
            Member findMember = em.find(Member.class, member.getId());
            System.out.println("findMember.id = " + findMember.getId());
            System.out.println("findMember.name = " + findMember.getName());
            System.out.println("findMember.workPeriod = " + findMember.getWorkPeriod());
            System.out.println("findMember.homeAddress = " + findMember.getHomeAddress());

            etx.commit();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

    @Test
    @DisplayName("@Embeddable, @Embedded - 임베디드 타입이 NULL 인 경우 하위 속성값들도 모두 NULL 값을 가진다")
    public void embeddedTypeNullTest() {
        try {
            etx.begin();

            /* Member Entity 생성 및 영속화 */
            Member member = new Member();
            member.setName("ryan");
            member.setWorkPeriod(null); // Period Embedded Type 을 Null 로 설정
            member.setHomeAddress(new Address("homeCity", "homeStreet", "homeZipcode"));
            em.persist(member);

            /* select query 를 확인하기 위한 목적으로 추가한 코드 */
            em.flush();
            em.clear();

            /* 영속화한 Member Entity 조회 */
            Member findMember = em.find(Member.class, member.getId());
            assertThat(findMember.getName()).isEqualTo("ryan");
            assertThat(findMember.getHomeAddress().getCity()).isEqualTo("homeCity");
            assertThat(findMember.getHomeAddress().getStreet()).isEqualTo("homeStreet");
            assertThat(findMember.getHomeAddress().getZipcode()).isEqualTo("homeZipcode");
            assertThat(findMember.getWorkPeriod()).isNull();

            etx.commit();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

}
