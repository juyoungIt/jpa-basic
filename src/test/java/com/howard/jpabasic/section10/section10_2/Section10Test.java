package com.howard.jpabasic.section10.section10_2;

import jakarta.persistence.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@EntityScan(basePackages = {"com.howard.jpabasic.section10.section10_2"})
public class Section10Test {

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
    @DisplayName("query.getResultList() - 결과가 하나 이상일 때, 리스트를 반환")
    public void resultListWhenMoreThanOneResultTest() {
        try {
            etx.begin();

            /* 테스트를 위해 사전 데이터 세팅 */
            Member memberA = new Member();
            memberA.setUsername("ryan");
            memberA.setAge(25);
            em.persist(memberA);

            Member memberB = new Member();
            memberB.setUsername("choonsik");
            memberB.setAge(24);
            em.persist(memberB);

            Member memberC = new Member();
            memberC.setUsername("apeach");
            memberC.setAge(18);
            em.persist(memberC);

            /* 나이가 20세 이상인 회원을 조회한다 - 결과가 하나 이상인 경우 */
            List<Member> results = em
                    .createQuery("select m " +
                            "from com.howard.jpabasic.section10.section10_2.Member m " +
                            "where m.age >= 20",
                            Member.class)
                    .getResultList();

            assertThat(results).isEqualTo(List.of(memberA, memberB));

            etx.rollback();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

    @Test
    @DisplayName("query.getResultList() - 결과가 없는 경우 빈 리스트를 반환")
    public void resultListWhenNoResultTest() {
        try {
            etx.begin();

            /* 테스트를 위해 사전 데이터 세팅 */
            Member memberA = new Member();
            memberA.setUsername("ryan");
            memberA.setAge(25);
            em.persist(memberA);

            Member memberB = new Member();
            memberB.setUsername("choonsik");
            memberB.setAge(24);
            em.persist(memberB);

            Member memberC = new Member();
            memberC.setUsername("apeach");
            memberC.setAge(18);
            em.persist(memberC);

            /* 나이가 30세 이상인 회원을 조회한다 - 결과가 하나 이상인 경우 */
            List<Member> results = em
                    .createQuery("select m " +
                            "from com.howard.jpabasic.section10.section10_2.Member m " +
                            "where m.age >= 30",
                            Member.class)
                    .getResultList();

            assertThat(results).isEmpty();

            etx.rollback();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

    @Test
    @DisplayName("query.getSingleResult() - 결과가 정확히 하나인 경우, 단일 객체를 반환한다.")
    public void singleResultWhenExactlyOneResultTest() {
        try {
            etx.begin();

            /* 테스트를 위해 사전 데이터 세팅 */
            Member memberA = new Member();
            memberA.setUsername("ryan");
            memberA.setAge(25);
            em.persist(memberA);

            Member memberB = new Member();
            memberB.setUsername("choonsik");
            memberB.setAge(24);
            em.persist(memberB);

            Member memberC = new Member();
            memberC.setUsername("apeach");
            memberC.setAge(18);
            em.persist(memberC);

            /* 이름이 'ryan' 인 회원을 찾는다. */
            Member result = em
                    .createQuery("select m " +
                            "from com.howard.jpabasic.section10.section10_2.Member m " +
                            "where m.username = 'ryan'",
                            Member.class)
                    .getSingleResult();

            assertThat(result.getUsername()).isEqualTo("ryan");

            etx.rollback();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

    @Test
    @DisplayName("query.getSingleResult() - 결과가 없으면 : NoResultException 발생")
    public void singleResultWhenEmptyResultTest() {
        try {
            etx.begin();

            /* 테스트를 위해 사전 데이터 세팅 */
            Member memberA = new Member();
            memberA.setUsername("ryan");
            memberA.setAge(25);
            em.persist(memberA);

            Member memberB = new Member();
            memberB.setUsername("choonsik");
            memberB.setAge(24);
            em.persist(memberB);

            Member memberC = new Member();
            memberC.setUsername("apeach");
            memberC.setAge(18);
            em.persist(memberC);

            /* 이름이 'brown' 인 회원을 찾는다. */
            assertThatThrownBy(() -> em
                    .createQuery("select m " +
                            "from com.howard.jpabasic.section10.section10_2.Member m " +
                            "where m.username = 'brown'",
                            Member.class)
                    .getSingleResult())
                    .isInstanceOf(NoResultException.class);

            etx.rollback();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

    @Test
    @DisplayName("query.getSingleResult() - 결과가 둘 이상이면 : NonUniqueResultException 발생")
    public void singleResultWhenMultipleResultTest() {
        try {
            etx.begin();

            /* 테스트를 위해 사전 데이터 세팅 */
            Member memberA = new Member();
            memberA.setUsername("ryan");
            memberA.setAge(25);
            em.persist(memberA);

            Member memberB = new Member();
            memberB.setUsername("choonsik");
            memberB.setAge(24);
            em.persist(memberB);

            Member memberC = new Member();
            memberC.setUsername("apeach");
            memberC.setAge(18);
            em.persist(memberC);

            /* 이름의 길이가 6자 이상 인 회원을 찾는다. */
            assertThatThrownBy(() -> em
                    .createQuery("select m " +
                            "from com.howard.jpabasic.section10.section10_2.Member m " +
                            "where length(m.username) >= 6",
                            Member.class)
                    .getSingleResult())
                    .isInstanceOf(NonUniqueResultException.class);

            etx.rollback();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

}
