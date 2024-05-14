package com.howard.jpabasic.section9.section9_2.relation;

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

@SpringBootTest
@EntityScan(basePackages = {"com.howard.jpabasic.section9.section9_2.relation"})
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
    @DisplayName("@Embeddable, @Embedded - 임베디드 타입이 Entity 를 값으로 가질 수 있다.")
    public void embeddedTestWithRelation() {
        try {
            etx.begin();

            /* History Entity 생성 및 영속화 */
            History history = new History();
            history.setLog("iksan -> pohang -> suwon");
            em.persist(history);

            /* Member Entity 생성 및 영속화 */
            Member member = new Member();
            member.setName("ryan");
            member.setWorkPeriod(new Period(
                    LocalDateTime.of(2023, 2, 1, 0, 0, 0),
                    LocalDateTime.now()
            ));
            member.setHomeAddress(new Address("homeCity", "homeStreet", "homeZipcode", history));
            em.persist(member);

            /* select query 를 확인하기 위한 목적으로 추가한 코드 */
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

}
