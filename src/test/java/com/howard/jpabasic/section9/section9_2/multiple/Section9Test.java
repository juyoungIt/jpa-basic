package com.howard.jpabasic.section9.section9_2.multiple;

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
@EntityScan(basePackages = {"com.howard.jpabasic.section9.section9_2.multiple"})
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
    @DisplayName("@AttributeOverrides, @AttributeOverride - 한 엔티티 내에서 같은 속성값을 사용할 수 있다")
    public void embeddedTypeTestWhenMultipleSameType() {
        try {
            etx.begin();

            /* Member Entity 생성 및 영속화 */
            Member member = new Member();
            member.setName("ryan");
            member.setWorkPeriod(new Period(
                    LocalDateTime.of(2023, 2, 1, 0, 0, 0),
                    LocalDateTime.now()
            ));
            member.setHomeAddress(new Address("homeCity", "homeStreet", "homeZipcode"));
            member.setCompanyAddress(new Address("companyCity", "companyStreet", "companyZipcode"));
            em.persist(member);

            /* select query 를 확인하기 위한 목적으로 추가한 코드 */
            em.flush();
            em.clear();

            /* 영속화된 Member Entity 조회 */
            Member findMember = em.find(Member.class, member.getId());
            System.out.println("findMember.id = " + findMember.getId());
            System.out.println("findMember.name = " + findMember.getName());
            System.out.println("findMember.workPeriod = " + findMember.getWorkPeriod());
            System.out.println("findMember.homeAddress = " + findMember.getHomeAddress());
            System.out.println("findMember.companyAddress = " + findMember.getCompanyAddress());

            etx.commit();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

}
