package com.howard.jpabasic.section10.section10_5;

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

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@EntityScan(basePackages = {"com.howard.jpabasic.section10.section10_5"})
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
    @DisplayName("INNER JOIN")
    public void innerJoinTest() {
        try {
            etx.begin();

            Team teamA = new Team();
            teamA.setName("teamA");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("teamB");
            em.persist(teamB);

            Member memberA = new Member();
            memberA.setUsername("memberA");
            memberA.setAge(20);
            memberA.setTeamRelation(teamA);
            em.persist(memberA);

            Member memberB = new Member();
            memberB.setUsername("memberB");
            memberB.setAge(30);
            memberB.setTeamRelation(teamB);
            em.persist(memberB);

            List<MemberDTO> findMembers = em
                    .createQuery(
                            "select new com.howard.jpabasic.section10.section10_5.MemberDTO(m.username, m.age, t.name) " +
                                    "from com.howard.jpabasic.section10.section10_5.Member m " +
                                    "join m.team t",
                            MemberDTO.class
                    )
                    .getResultList();

            assertThat(findMembers).hasSize(3);
            assertThat(findMembers).extracting(MemberDTO::getUsername).containsExactly("memberA", "memberB");
            assertThat(findMembers).extracting(MemberDTO::getAge).containsExactly(20, 30);
            assertThat(findMembers).extracting(MemberDTO::getTeamName).contains("teamA", "teamB");

            etx.rollback();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

    @Test
    @DisplayName("OUTER JOIN")
    public void outerJoinTest() {
        try {
            etx.begin();

            Team teamA = new Team();
            teamA.setName("teamA");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("teamB");
            em.persist(teamB);

            Member memberA = new Member();
            memberA.setUsername("memberA");
            memberA.setAge(20);
            memberA.setTeamRelation(teamA);
            em.persist(memberA);

            Member memberB = new Member();
            memberB.setUsername("memberB");
            memberB.setAge(30);
            memberB.setTeamRelation(teamB);
            em.persist(memberB);

            Member memberC = new Member();
            memberC.setUsername("memberC");
            memberC.setAge(40);
            em.persist(memberC);

            List<MemberDTO> findMembers = em
                    .createQuery(
                            "select new com.howard.jpabasic.section10.section10_5.MemberDTO(m.username, m.age, t.name) " +
                                    "from com.howard.jpabasic.section10.section10_5.Member m " +
                                    "left join m.team t",
                            MemberDTO.class
                    )
                    .getResultList();

            assertThat(findMembers).hasSize(3);
            assertThat(findMembers).extracting(MemberDTO::getUsername).containsExactly("memberA", "memberB", "memberC");
            assertThat(findMembers).extracting(MemberDTO::getAge).containsExactly(20, 30, 40);
            assertThat(findMembers).extracting(MemberDTO::getTeamName).contains("teamA", "teamB", null);

            etx.rollback();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

    @Test
    @DisplayName("THETA JOIN (Cartesian Product)")
    public void thetaJoinTest() {
        try {
            etx.begin();

            Team teamA = new Team();
            teamA.setName("groupA");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("groupB");
            em.persist(teamB);

            Member memberA = new Member();
            memberA.setUsername("groupA");
            memberA.setAge(20);
            em.persist(memberA);

            Member memberB = new Member();
            memberB.setUsername("groupB");
            memberB.setAge(30);
            em.persist(memberB);

            Member memberC = new Member();
            memberC.setUsername("groupC");
            memberC.setAge(40);
            em.persist(memberB);

            List<MemberDTO> findMembers = em
                    .createQuery(
                            "select new com.howard.jpabasic.section10.section10_5.MemberDTO(m.username, m.age, t.name) " +
                                    "from com.howard.jpabasic.section10.section10_5.Member m, com.howard.jpabasic.section10.section10_5.Team t " +
                                    "where m.username = t.name",
                            MemberDTO.class
                    )
                    .getResultList();

            assertThat(findMembers).hasSize(2);
            assertThat(findMembers)
                    .extracting(MemberDTO::getUsername)
                    .containsExactly("groupA", "groupB");
            assertThat(findMembers)
                    .extracting(MemberDTO::getAge)
                    .containsExactly(20, 30);
            assertThat(findMembers)
                    .extracting(MemberDTO::getTeamName)
                    .containsExactly("groupA", "groupB");

            etx.rollback();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

    @Test
    @DisplayName("ON 절을 활용한 조인 - 조인 대상 필터링")
    public void joinOnTest1() {
        try {
            etx.begin();

            Team teamA = new Team();
            teamA.setName("teamA");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("teamB");
            em.persist(teamB);

            Member memberA = new Member();
            memberA.setUsername("memberA");
            memberA.setAge(20);
            em.persist(memberA);

            Member memberB = new Member();
            memberB.setUsername("memberB");
            memberB.setAge(30);
            em.persist(memberB);

            Member memberC = new Member();
            memberC.setUsername("memberC");
            memberC.setAge(40);
            em.persist(memberC);

            List<MemberDTO> findMembers = em
                    .createQuery(
                            "select new com.howard.jpabasic.section10.section10_5.MemberDTO(m.username, m.age, t.name) " +
                                    "from com.howard.jpabasic.section10.section10_5.Member m " +
                                    "left join m.team t on m.username = 'memberA'",
                            MemberDTO.class
                    )
                    .getResultList();

            assertThat(findMembers).hasSize(3);
            assertThat(findMembers)
                    .extracting(MemberDTO::getUsername)
                    .containsExactly("memberA", "memberB", "memberC");
            assertThat(findMembers)
                    .extracting(MemberDTO::getAge)
                    .containsExactly(20, 30, 40);
            assertThat(findMembers)
                    .extracting(MemberDTO::getTeamName)
                    .containsExactly("teamA", null, null);

            etx.rollback();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

    @Test
    @DisplayName("ON 절을 활용한 조인 - 연관관계 없는 엔티 외부 조인")
    public void joinOnTest2() {
        try {
            etx.begin();

            Team teamA = new Team();
            teamA.setName("groupA");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("groupB");
            em.persist(teamB);

            Member memberA = new Member();
            memberA.setUsername("groupA");
            memberA.setAge(20);
            memberA.setTeamRelation(teamA);
            em.persist(memberA);

            Member memberB = new Member();
            memberB.setUsername("groupB");
            memberB.setAge(30);
            memberB.setTeamRelation(teamB);
            em.persist(memberB);

            Member memberC = new Member();
            memberC.setUsername("groupC");
            memberC.setAge(40);
            em.persist(memberC);

            List<MemberDTO> findMembers = em
                    .createQuery(
                            "select new com.howard.jpabasic.section10.section10_5.MemberDTO(m.username, m.age, t.name) " +
                                    "from com.howard.jpabasic.section10.section10_5.Member m " +
                                    "join com.howard.jpabasic.section10.section10_5.Team t on m.username = t.name",
                            MemberDTO.class
                    )
                    .getResultList();

            assertThat(findMembers).hasSize(2);
            assertThat(findMembers)
                    .extracting(MemberDTO::getUsername)
                    .containsExactly("groupA", "groupB");
            assertThat(findMembers)
                    .extracting(MemberDTO::getAge)
                    .containsExactly(20, 30);
            assertThat(findMembers)
                    .extracting(MemberDTO::getTeamName)
                    .containsExactly("groupA", "groupB");

            etx.rollback();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

}
