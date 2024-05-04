package com.howard.jpabasic.section6.section6_2;

import com.howard.jpabasic.section6.section6_2.one_way.MemberOneWay;
import com.howard.jpabasic.section6.section6_2.one_way.TeamOneWay;
import com.howard.jpabasic.section6.section6_2.two_way.MemberTwoWay;
import com.howard.jpabasic.section6.section6_2.two_way.TeamTwoWay;
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
@EntityScan(basePackages = {
        "com.howard.jpabasic.section6.section6_2.one_way",
        "com.howard.jpabasic.section6.section6_2.two_way"
})
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
    @DisplayName("1:N 단방향 - 단방향 연관관계를 가지는 Entity 생성 및 저장, 조회")
    public void oneWayOneToManyRelationTest() {
        try {
            etx.begin();

            /* MemberOneWay Entity 생성 및 영속화 */
            MemberOneWay member = new MemberOneWay();
            member.setUsername("ryan");
            em.persist(member);

            /* TeamOneWay Entity 생성 및 영속화 */
            TeamOneWay team = new TeamOneWay();
            team.setName("teamA");
            team.getMembers().add(member);
            em.persist(team);

            /* select query 를 확인하기 위한 목적으로 추가 */
            em.flush();
            em.clear();

            /* JPA 의 1:N -> 1이 연관관계의 주인이 됨 */
            /* RDB 의 1:N -> 항상 N 쪽이 외래키를 관리하게 됨 */
            /* -> 즉, 이러한 차이로 인해서 반대편 테이블에서 외래키를 관리하는 독특한 구조가 된다. */
            /* -> @JoinColumn 을 꼭 사용해야 한다. 그렇지 않으면 조인 테이블 방식을 사용한다.(중간에 테이블을 하나 추가함) */
            /* -> 그래서 실제로 연관관계 관리를 위해 매번 UPDATE Query 가 추가로 나가는 것을 확인할 수 있다. */

            /* 단방향 연관관계를 가지는 Member Entity 조회 */
            TeamOneWay findTeam = em.find(TeamOneWay.class, team.getId());
            System.out.println("findTeam.id = " + findTeam.getId());
            System.out.println("findTeam.name = " + findTeam.getName());
            System.out.println("findTeam.members = " + findTeam.getMembers());

            etx.commit();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

    @Test
    @DisplayName("1:N 양방향 - 양방향 연관관계를 가지는 Entity 생성 및 저장, 조회")
    public void twoWayOneToManyRelationTest() {
        try {
            etx.begin();

            /* MemberTwoWay Entity 생성 및 영속화 */
            MemberTwoWay member = new MemberTwoWay();
            member.setUsername("ryan");
            em.persist(member);

            /* TeamTwoWay Entity 생성 및 영속화 */
            TeamTwoWay team = new TeamTwoWay();
            team.setName("teamA");
            team.setMembersRelation(member); // 유틸리티 메서드 활용
            em.persist(team);

            /* select query 를 확인하기 위한 목적으로 추가 */
            em.flush();
            em.clear();

            /* JPA 의 1:N -> 1이 연관관계의 주인이 됨 */
            /* RDB 의 1:N -> 항상 N 쪽이 외래키를 관리하게 됨 */
            /* -> 즉, 이러한 차이로 인해서 반대편 테이블에서 외래키를 관리하는 독특한 구조가 된다. */
            /* -> @JoinColumn 을 꼭 사용해야 한다. 그렇지 않으면 조인 테이블 방식을 사용한다.(중간에 테이블을 하나 추가함) */
            /* -> 그래서 실제로 연관관계 관리를 위해 매번 UPDATE Query 가 추가로 나가는 것을 확인할 수 있다. */

            /* 양방향 연관관계를 가지는 Member Entity 조회 */
            TeamTwoWay findTeam = em.find(TeamTwoWay.class, team.getId());
            System.out.println("findTeam.id = " + findTeam.getId());
            System.out.println("findTeam.name = " + findTeam.getName());
            for (MemberTwoWay m : findTeam.getMembers()) {
                System.out.println("member.id = " + m.getId());
                System.out.println("member.username = " + m.getUsername());
                System.out.println("member.team.id = " + m.getTeam().getId());
                System.out.println("member.team.name = " + m.getTeam().getName());
                System.out.println("member.team.members.size() = " + m.getTeam().getMembers().size());
            }

            etx.commit();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

}
