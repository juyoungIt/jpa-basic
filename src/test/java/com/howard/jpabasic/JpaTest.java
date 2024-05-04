package com.howard.jpabasic;

import jakarta.persistence.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JpaTest {

    @PersistenceUnit
    private EntityManagerFactory emf;
    private EntityManager em;
    private EntityTransaction tx;

    @BeforeEach
    public void init() {
        em = emf.createEntityManager();
        tx = em.getTransaction();
    }

    @Test
    @DisplayName("환경이 제대로 구성되었는 지 확인하기 위한 용도의 코드")
    public void initTest() {
        try {
            tx.begin();

            /* MemberA */
            Member memberA = new Member();
            memberA.setUsername("ryan");
            memberA.setAge(25);
            em.persist(memberA);

            /* MemberB */
            Member memberB = new Member();
            memberB.setUsername("choonsik");
            memberB.setAge(20);
            em.persist(memberB);

            Member findMemberA = em.find(Member.class, memberA.getId());
            Member findMemberB = em.find(Member.class, memberB.getId());

            System.out.println("findMemberA.id = " + findMemberA.getId());
            System.out.println("findMemberA.username = " + findMemberA.getUsername());
            System.out.println("findMemberA.age = " + findMemberA.getAge());

            System.out.println("findMemberB.id = " + findMemberB.getId());
            System.out.println("findMemberB.username = " + findMemberB.getUsername());
            System.out.println("findMemberB.age = " + findMemberB.getAge());

            tx.commit();
        } catch(Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
    }

}
