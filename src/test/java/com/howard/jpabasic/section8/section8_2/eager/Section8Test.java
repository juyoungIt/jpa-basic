package com.howard.jpabasic.section8.section8_2.eager;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@EntityScan(basePackages = {"com.howard.jpabasic.section8.section8_2.eager"})
public class Section8Test {

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
    @DisplayName("FetchType.EAGER - 즉시 로딩을 사용해서 모두 한번에 조회한다.")
    public void fetchTypeEagerTest() {
        try {
            etx.begin();

            /* Team Entity 생성 및 영속화 */
            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            /* Member Entity 생성 및 영속화 */
            Member member = new Member();
            member.setName("ryan");
            member.setTeamRelation(team); // 유틸리티 메서드 사용
            em.persist(member);

            /* select query 를 확인하기 위한 목적으로 추가한 코드 */
            em.flush();
            em.clear();

            /* Team 에 대해서 FetchType.EAGER 로 설정하고, Member 만 먼저 조회 */
            /* -> 즉시로딩이기 때문에 이 시점에 이미 JOIN 해서 한번에 로딩한다. */
            Member findMember = em.find(Member.class, member.getId());
            System.out.println("findMember.getClass().getName() = " + findMember.getClass().getName());
            System.out.println("findMember.getTeam().getClass().getName() = " + findMember.getTeam().getClass().getName());
            /* 때문에 이 지점에서도 Team 을 따로 로딩하는 일은 발생하지 않는다. -> 이미 로딩 되었기 때문에 프록시 초기화가 필요 없는 것 */
            System.out.println("-------------------------");
            System.out.println("findMember.team.id = " + findMember.getTeam().getId());
            System.out.println("findMember.team.name = " + findMember.getTeam().getName());
            System.out.println("-------------------------");

            etx.commit();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

    @Test
    @DisplayName("FetchType.EAGER - 즉시 로딩은 JPQL 에서 N+1 문제를 일으킨다.")
    public void nPlusOneProblemTest() {
        try {
            etx.begin();

            /* Team Entity 생성 및 영속화 */
            Team teamA = new Team();
            teamA.setName("teamA");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("teamB");
            em.persist(teamB);

            Team teamC = new Team();
            teamC.setName("teamC");
            em.persist(teamC);

            Team teamD = new Team();
            teamD.setName("teamD");
            em.persist(teamD);

            Team teamE = new Team();
            teamE.setName("teamE");
            em.persist(teamE);

            /* Member Entity 생성 및 영속화 */
            Member memberA = new Member();
            memberA.setName("memberA");
            memberA.setTeamRelation(teamA);
            em.persist(memberA);

            Member memberB = new Member();
            memberB.setName("memberB");
            memberB.setTeamRelation(teamB);
            em.persist(memberB);

            Member memberC = new Member();
            memberC.setName("memberC");
            memberC.setTeamRelation(teamC);
            em.persist(memberC);

            Member memberD = new Member();
            memberD.setName("memberD");
            memberD.setTeamRelation(teamD);
            em.persist(memberD);

            Member memberE = new Member();
            memberE.setName("memberE");
            memberE.setTeamRelation(teamE);
            em.persist(memberE);

            /* select query 를 확인하기 위한 목적으로 추가한 코드 */
            em.flush();
            em.clear();

            /* JPQL 을 사용해서 모든 Member Entity 를 조회 */
            /* -> 이 부분에서 N+1 문제가 발생하게 된다. */
            List<Member> members = em.createQuery("select m from Member m", Member.class).getResultList();

            etx.commit();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

}
