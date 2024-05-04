package com.howard.jpabasic.section6.section6_1.two_way;

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
@EntityScan(basePackages = {"com.howard.jpabasic.section6.section6_1.two_way"})
public class Section6Test {

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
    @DisplayName("N:1 양방향 - 양방향 연관관계를 가지는 Entity 생성 및 저장, 조회")
    public void twoWayManyToOneRelationTest() {
        try {
            etx.begin();

            /* TeamTwoWay Entity 생성 및 영속화 */
            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            /* MemberTwoWay Entity 생성 및 영속화 */
            Member member = new Member();
            member.setUsername("ryan");
            member.setTeamRelation(team); // 유틸리티 메서드 활용
            em.persist(member);

            /* select query 를 확인하기 위한 목적으로 추가 */
            em.flush();
            em.clear();

            /* 단방향 연관관계를 가지는 Member Entity 조회 */
            Member findMember = em.find(Member.class, member.getId());
            System.out.println("findMember.id = " + findMember.getId());
            System.out.println("findMember.username = " + findMember.getUsername());
            System.out.println("findMember.team.id = " + findMember.getTeam().getId());
            System.out.println("findMember.team.name = " + findMember.getTeam().getName());
            System.out.println("findMember.team.members.size() = " + findMember.getTeam().getMembers().size());

            etx.commit();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

}
