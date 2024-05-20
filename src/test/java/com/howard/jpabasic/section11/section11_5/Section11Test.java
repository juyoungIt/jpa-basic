package com.howard.jpabasic.section11.section11_5;

import jakarta.persistence.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@EntityScan(basePackages = {"com.howard.jpabasic.section11.section11_5"})
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
    @DisplayName("엔티티 직접 사용 - JPQL 에서 엔티티를 직접 사용하면 SQL 에서 해당 엔티티의 기본키 값을 사용한다.")
    public void entityUseToPrimaryKeyTest() {
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

            /* 두 쿼리를 사실 상 서로 동일한 쿼리가 된다 */
            Member findMemberA = em
                    .createQuery("select m from Member m where m.id = :memberId", Member.class)
                    .setParameter("memberId", memberA.getId())
                    .getSingleResult();

            Member findMemberB = em
                    .createQuery("select m from Member m where m = :member", Member.class)
                    .setParameter("member", memberA)
                    .getSingleResult();

            assertThat(findMemberA).isEqualTo(findMemberB);

            etx.rollback();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

    @Test
    @DisplayName("엔티티 직접 사용 - JPQL 에서 엔티티를 직접 사용하면 SQL 에서 해당 엔티티의 기본키 값을 사용한다. -> FK 로도 적용된다.")
    public void entityUseToForeignKeyTest() {
        try {
            etx.begin();

            Team teamA = new Team();
            teamA.setName("teamA");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("teamB");
            em.persist(teamB);

            Member memberA = new Member();
            memberA.setName("memberA");
            memberA.setTeamRelation(teamA);
            em.persist(memberA);

            Member memberB = new Member();
            memberB.setName("memberB");
            memberB.setTeamRelation(teamB);
            em.persist(memberB);

            /* 마찬가지로 두 쿼리를 서로 동일한 쿼리가 된다 */
            Member findMemberA = em
                    .createQuery(
                            "select m from com.howard.jpabasic.section11.section11_5.Member m where m.team.id = :teamId",
                            Member.class
                    )
                    .setParameter("teamId", teamA.getId())
                    .getSingleResult();

            Member findMemberB = em
                    .createQuery(
                            "select m from com.howard.jpabasic.section11.section11_5.Member m where m.team = :team",
                            Member.class
                    )
                    .setParameter("team", teamA)
                    .getSingleResult();

            assertThat(findMemberA).isEqualTo(findMemberB);

            etx.rollback();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

}
