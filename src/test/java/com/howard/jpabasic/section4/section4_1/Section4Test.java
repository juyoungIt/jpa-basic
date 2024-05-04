package com.howard.jpabasic.section4.section4_1;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@EntityScan(basePackages = {"com.howard.jpabasic.section4.section4_1"})
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
    @DisplayName("@Table(name = {}) 를 통해서 DB에 생성할 Table 명을 커스텀할 수 있다.")
    public void tableNameCustomTest() {
        try {
            etx.begin();

            /* 단순히 생성되는 Table 명을 보기 위해 작성한 코드 */
            /* -> 기존처럼 Member 가 아닌 지정한 MBR 이라는 이름으로 생성된다. */
            Member member = new Member();
            member.setUsername("ryan");
            member.setAge(25);
            em.persist(member);

            etx.commit();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

    @Test
    @DisplayName("@Column(name = {}) 를 통해서 DB에 생성할 Table Column 을 커스텀할 수 있다.")
    public void columnNameCustomTest() {
        try {
            etx.begin();

            Member member = new Member();
            member.setUsername("ryan");
            member.setAge(25);
            em.persist(member);

            /* Query에서 기존에 설정한 username이 아닌 지정한 name으로 column 명이 사용된다. */
            Member findMember = em.find(Member.class, member.getId());
            System.out.println("findMember.id = " + findMember.getId());
            System.out.println("findMember.name = " + findMember.getUsername());
            System.out.println("findMember.age = " + findMember.getAge());

            etx.commit();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

    @Test
    @DisplayName("@Column(unique = true) 를 통해서 DB에 생성할 Table Column 에 UNIQUE 속성을 부여할 수 있다.")
    public void uniqueColumnTest() {
        try {
            etx.begin();

            /* 단순히 생성되는 Table 명을 보기 위해 작성한 코드 */
            Member memberA = new Member();
            memberA.setUsername("ryan");
            memberA.setAge(25);
            em.persist(memberA);

            /* 동일한 이름의 회원정보를 하나 더 저장한다. */
            Member memberB = new Member();
            memberB.setUsername("ryan");
            memberB.setAge(20);
            em.persist(memberB);

            etx.commit();
        } catch (Exception e) {
            System.out.println("중복된 이름이어서 영속화에 실패함");
            e.printStackTrace();
            etx.rollback();
        } finally {
            em.close();
        }
    }

    @Test
    @DisplayName("@Column(length = {}) 를 통해서 DB에 생성할 Table Column 에 길이 제한을 부여할 수 있다.")
    public void columnLengthLimitTest() {
        try {
            etx.begin();

            /* 제약조건으로 설정한 길이보다 긴 이름을 저장하려는 경우 */
            Member memberA = new Member();
            memberA.setUsername("choonsik");
            memberA.setAge(25);
            em.persist(memberA);

            etx.commit();
        } catch (Exception e) {
            System.out.println("이름의 길이가 5를 초과하여 영속화에 실패함");
            e.printStackTrace();
            etx.rollback();
        } finally {
            em.close();
        }
    }

}
