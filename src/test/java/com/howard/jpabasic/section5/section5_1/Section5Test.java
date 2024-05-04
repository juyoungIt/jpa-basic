package com.howard.jpabasic.section5.section5_1;

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
@EntityScan(basePackages = {"com.howard.jpabasic.section5.section5_1"})
public class Section5Test {

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
    @DisplayName("단방향 연관관계 - 연관관계를 설정하여 저장 및 조회한다.")
    public void oneWayRelationTest() {
        try {
            etx.begin();

            /* Team Entity 생성 및 저장 */
            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            /* Member Entity 생성 및 저장 */
            Member member = new Member();
            member.setUsername("ryan");
            member.setAge(25);
            member.setTeam(team);
            em.persist(member);

            /* select query 를 보기 위한 목적으로 추가 */
            em.flush();
            em.clear();

            Member findMember = em.find(Member.class, member.getId());
            System.out.println("findMember.id = " + findMember.getId());
            System.out.println("findMember.username = " + findMember.getUsername());
            System.out.println("findMember.age = " + findMember.getAge());
            System.out.println("findMember.team = " + findMember.getTeam());

            etx.commit();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

    @Test
    @DisplayName("단방향 연관관계 - 연관관계를 설정하여 저장 및 조회 후 연관관계를 수정하여 다시 조회한다.")
    public void oneWayRelationWithEditingTest() {
        try {
            etx.begin();

            /* Team Entity 생성 및 저장 */
            Team teamA = new Team();
            teamA.setName("teamA");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("teamB");
            em.persist(teamB);

            /* Member Entity 생성 및 저장 */
            Member member = new Member();
            member.setUsername("ryan");
            member.setAge(25);
            member.setTeam(teamA);
            em.persist(member);

            /* select query 를 보기 위한 목적으로 추가 */
            em.flush();
            em.clear();

            Member findMember = em.find(Member.class, member.getId());
            System.out.println("findMember.id = " + findMember.getId());
            System.out.println("findMember.username = " + findMember.getUsername());
            System.out.println("findMember.age = " + findMember.getAge());
            System.out.println("findMember.team = " + findMember.getTeam());

            /* 연관관계를 수정하여 다시 조회 */
            findMember.setTeam(teamB);

            /* update query 를 보기 위한 목적으로 추가 */
            em.flush();
            em.clear();

            findMember = em.find(Member.class, member.getId());
            System.out.println("findMember.id = " + findMember.getId());
            System.out.println("findMember.username = " + findMember.getUsername());
            System.out.println("findMember.age = " + findMember.getAge());
            System.out.println("findMember.team = " + findMember.getTeam());

            etx.commit();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

}
