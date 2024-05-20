package com.howard.jpabasic.section11.section11_1;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@EntityScan(basePackages = {"com.howard.jpabasic.section11.section11_1"})
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
    @DisplayName("경로 표현식 - 상태필드, 경로 탐색의 끝이며 더 이상 탐색이 불가능하다.")
    public void pathExpressionStateFieldTest() {
        try {
            etx.begin();

            Member member = new Member();
            member.setName("ryan");
            em.persist(member);

            List<String> results = em
                    .createQuery(
                            "select m.name from com.howard.jpabasic.section11.section11_1.Member m",
                            String.class
                    )
                    .getResultList();

            assertThat(results).containsExactly("ryan");

            etx.rollback();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

    @Test
    @DisplayName("경로 표현식 - 단일 값 연관경로, 묵시적 내부 조인이 발생하며 지속적인 탐색이 가능하다.")
    public void pathExpressionSingleValueRelationPathTest() {
        try {
            etx.begin();

            Team team = new Team();
            team.setName("friends");
            em.persist(team);

            Member member = new Member();
            member.setName("ryan");
            member.setTeamRelation(team);
            em.persist(member);

            List<String> results = em
                    .createQuery(
                            "select m.team.name from com.howard.jpabasic.section11.section11_1.Member m",
                            String.class
                    )
                    .getResultList();

            assertThat(results).containsExactly("friends");

            etx.rollback();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

    @Test
    @DisplayName("경로 표현식 - 컬렉션 값 연관경로, 묵시적 내부 조인이 발생하며 지속적인 탐색이 불가능하다.")
    public void pathExpressionCollectionValueRelationPathTest() {
        try {
            etx.begin();

            Team team = new Team();
            team.setName("friends");
            em.persist(team);

            Member memberA = new Member();
            memberA.setName("ryan");
            memberA.setTeamRelation(team);
            em.persist(memberA);

            Member memberB = new Member();
            memberB.setName("choonsik");
            memberB.setTeamRelation(team);
            em.persist(memberB);

            List<Collections> results = em
                    .createQuery(
                            "select t.members from com.howard.jpabasic.section11.section11_1.Team t",
                            Collections.class
                    )
                    .getResultList();

            assertThat(results).hasSize(2);
            // Collections 의 처리가 까다로워서 나머지 테스트 코드는 생략

            etx.rollback();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

    @Test
    @DisplayName("경로 표현식 - 컬렉션 값 연관경로, 명시적 조인을 통해 별칭을 얻으면 별칭을 통해서 탐색이 가능하다")
    public void pathExpressionCollectionValueRelationPathSolutionTest() {
        try {
            etx.begin();

            Team team = new Team();
            team.setName("friends");
            em.persist(team);

            Member memberA = new Member();
            memberA.setName("ryan");
            memberA.setTeamRelation(team);
            em.persist(memberA);

            Member memberB = new Member();
            memberB.setName("choonsik");
            memberB.setTeamRelation(team);
            em.persist(memberB);

            List<String> results = em
                    .createQuery(
                            "select m.name from com.howard.jpabasic.section11.section11_1.Team t join t.members m",
                            String.class
                    )
                    .getResultList();

            assertThat(results).hasSize(2);
            assertThat(results).containsExactly("ryan", "choonsik");

            etx.rollback();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

    /*
     * 명시적 조인 vs 묵시적 조인
     * 1. 명시적 조인 : join 키워드를 직접 사용하여 조인을 수행하는 것
     * 2. 묵시적 조인 : 경로 표현식에 의해 묵시적으로 SQL 조인이 발생하는 것 (내부 조인만 가능)
     *
     * 경로 탐색을 통한 묵시적 조인 시 주의할 사항
     * 1. 경로 탐색에 의한 묵시적 조인은 항상 내부조인
     * 2. 컬렉션은 경로 탐색의 끝, 명시적 조인을 통해서 별칭을 얻어와야 한다
     * 3. 경로 탐색은 주로 select, where 절에서 사용하는 것이 일반적이지만 묵시적 조인으로 인해 SQL FROM 절에 영향을 줌
     *
     * 실무에서는!
     * 1. 가급적 묵시적 조인 대신에 명시적 조인을 사용할 것
     * 2. 조인은 SQL 튜닝 시 중요한 포인트 -> 묵시적 조인은 조인이 발생하는 상황을 한 눈에 확인하기 어려우므르 사용을 권장하지 않는다.
     * 3.
     *
     * */

}
