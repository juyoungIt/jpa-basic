package com.howard.jpabasic.section5.section5_2and3;

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
@EntityScan(basePackages = {"com.howard.jpabasic.section5.section5_2and3"})
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
    @DisplayName("양방향 연관관계 - 연관관계를 설정하여 저장 및 조회한다.")
    public void twoWayRelationTest() {
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
            member.setTeamRelation(team); // 유틸리티 메서드를 호출하여 사용
            em.persist(member);

            /* 양방향 연관관계 이므로 Team 쪽에도 Member 정보 추가 */
            /* -> 순수 객체상태를 고려해서 항상 양쪽에 값을 설정하자 */
            /* -> 누락하기 쉬운 부분이니 유틸리티 메서드를 구현해서 사용하도록 하자 */
            // team.getMembers().add(member);

            /* select query 를 보기 위한 목적으로 추가 */
            em.flush();
            em.clear();

            Member findMember = em.find(Member.class, member.getId());
            System.out.println("findMember.id = " + findMember.getId());
            System.out.println("findMember.username = " + findMember.getUsername());
            System.out.println("findMember.age = " + findMember.getAge());
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
