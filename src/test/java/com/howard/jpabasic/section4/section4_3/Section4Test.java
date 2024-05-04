package com.howard.jpabasic.section4.section4_3;

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
@EntityScan(basePackages = {"com.howard.jpabasic.section4.section4_3"})
public class Section4Test {

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
    @DisplayName("다양한 타입의 필드를 저장하는 테스트")
    public void insertTest() {
        try {
            etx.begin();

            Member memberA = new Member();
            memberA.setUsername("ryan");
            memberA.setAge(25);
            memberA.setRoleType(RoleType.ADMIN);
            memberA.setCreateDate(LocalDateTime.now());
            memberA.setModifiedDate(LocalDateTime.now());
            memberA.setDescription("테스트로 추가하는 첫번째 회원");
            em.persist(memberA);

            Member memberB = new Member();
            memberB.setUsername("choonsik");
            memberB.setAge(20);
            memberB.setRoleType(RoleType.COMMON);
            memberB.setCreateDate(LocalDateTime.now());
            memberB.setModifiedDate(LocalDateTime.now());
            memberB.setDescription("테스트로 추가하는 두번째 회원");
            em.persist(memberB);

            /* select query 를 보기 위해 사전에 flush 및 clear */
            em.flush();
            em.clear();

            Member findMemberA = em.find(Member.class, memberA.getId());
            Member findMemberB = em.find(Member.class, memberB.getId());
            System.out.println("findMemberA = " + findMemberA);
            System.out.println("findMemberB = " + findMemberB);

            etx.commit();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

}
