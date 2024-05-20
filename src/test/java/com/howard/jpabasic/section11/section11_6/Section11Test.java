package com.howard.jpabasic.section11.section11_6;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceUnit;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@EntityScan(basePackages = {"com.howard.jpabasic.section11.section11_6"})
public class Section11Test {

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
    @DisplayName("NamedQuery - 이를 통해 조금 더 심플한 코드 작성, 애플리케이션 로딩 시점에 쿼리 검증이 가능하다")
    public void namedQueryTest() {
        try {
            etx.begin();

            Member memberA = new Member();
            memberA.setName("memberA");
            em.persist(memberA);

            Member memberB = new Member();
            memberB.setName("memberB");
            em.persist(memberB);

            Member memberC = new Member();
            memberC.setName("memberC");
            em.persist(memberC);

            Member findMember = em.createNamedQuery("Member.findByName", Member.class)
                    .setParameter("name", "memberA")
                    .getSingleResult();

            assertThat(findMember).isEqualTo(memberA);
            assertThat(findMember.getName()).isEqualTo("memberA");

            etx.rollback();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

}
