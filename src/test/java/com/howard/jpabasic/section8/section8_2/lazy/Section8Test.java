package com.howard.jpabasic.section8.section8_2.lazy;

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
@EntityScan(basePackages = {"com.howard.jpabasic.section8.section8_2.lazy"})
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
    @DisplayName("FetchType.LAZY - 지연 로딩을 사용해서 프록시로 조회한다.")
    public void fetchTypeLazyTest() {
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

            /* Team 에 대해서 FetchType.LAZY 로 설정하고, Member 만 먼저 조회 */
            Member findMember = em.find(Member.class, member.getId());
            /* Member 만 먼저 조회하고, Team 은 프록시로 조회한 상태 */
            System.out.println("findMember.getClass().getName() = " + findMember.getClass().getName());
            System.out.println("findMember.getTeam().getClass().getName() = " + findMember.getTeam().getClass().getName());
            /* 하지만 여기서 Team 에 대한 접근이 발생하면 Team 도 select 로 가져오게 된다. */
            /* -> 즉, 실제 Team 을 사용하는 시점에 Team Proxy 에 대한 초기화가 발생한다. */
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

    /* 프록시와 즉시 로딩 개념에서 주의할 내용
     *
     * 1. 가급적 지연 로딩만 사용 (특히 실무에서)
     * 2. 즉시 로딩을 적용할 경우 예상치 못한 SQL 이 발생하는 경우가 발생한다.
     * 3. 즉시 로딩은 JPQL 에서 N+1 문제를 일으킨다.
     * 4. @ManyToOne, @OneToOne 은 기본값으로 즉시 로딩을 사용한다. -> 따라서 Lazy 로 설정해주자
     * 5. @OneToMany, @ManyToMany 는 기본값으로 지연 로딩을 사용한다.
     *
     * 결론! 실무에서는?
     * 1. 모든 연관관계에 대해서 지연 로딩을 적용해서 사용하자
     * 2. 실무에서는 즉시 로딩의 사용을 피하자
     * 3. JPQL fetch join 이나 entity graph 기능을 사용할 수 있도록 하자 (이는 나중에 뒤에서 학습)
     * 4. 즉시 로딩은 상상하지 못한 쿼리가 나갈 수 있기 때문에 주의가 필요하다.
     *
     *  */

}
