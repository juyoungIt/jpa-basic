package com.howard.jpabasic.section11.section11_2;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceUnit;
import org.junit.jupiter.api.*;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@EntityScan(basePackages = {"com.howard.jpabasic.section11.section11_2"})
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
    @DisplayName("Fetch JOIN Before - 페치 조인을 적용하기 이전 상황, N+1 문제가 발생한다.")
    public void fetchJoinBeforeTest() {
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
            memberB.setTeamRelation(teamA);
            em.persist(memberB);

            Member memberC = new Member();
            memberC.setName("memberC");
            memberC.setTeamRelation(teamB);
            em.persist(memberC);

            Member memberD = new Member();
            memberD.setName("memberD");
            em.persist(memberD);

            em.flush();
            em.clear();

            String query = "select m from com.howard.jpabasic.section11.section11_2.Member m";
            List<Member> findMembers = em.createQuery(query, Member.class).getResultList();

            assertThat(findMembers).hasSize(4);
            /* 다음과 같이 연관관계를 가진 team 에 접근하는 순간 추가적으로 Query 요청이 발생하면서 N+1 문제가 발생한다 */
            assertThat(findMembers)
                    .extracting(member -> member.getTeam().getName())
                    .containsExactly("teamA", "teamA", "teamB", null);

            etx.rollback();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

    @Test
    @DisplayName("Fetch JOIN After - 페치 조인을 적용 이후, N+1 문제가 발생하지 않는다.")
    public void fetchJoinAfterTest() {
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
            memberB.setTeamRelation(teamA);
            em.persist(memberB);

            Member memberC = new Member();
            memberC.setName("memberC");
            memberC.setTeamRelation(teamB);
            em.persist(memberC);

            Member memberD = new Member();
            memberD.setName("memberD");
            em.persist(memberD);

            em.flush();
            em.clear();

            /* 지연 로딩 세팅이 되어 있더라도 페치조인 설정이 항상 우선이다 */
            String query = "select m from com.howard.jpabasic.section11.section11_2.Member m left join fetch m.team";
            List<Member> findMembers = em.createQuery(query, Member.class).getResultList();

            assertThat(findMembers).hasSize(4);
            /* 지연로딩을 사용하지 않고, 조인하여 한번에 들고오기 때문에 모든 관련 내용들이 1차 캐시에 등록된다, */
            assertThat(findMembers)
                    .extracting(member -> member.getTeam().getName())
                    .containsExactly("teamA", "teamA", "teamB", null);

            etx.rollback();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

    @Test
    @DisplayName("-> 자료와 다르게 결과가 나타나는 현상... 컬랙션 페치조인 - 1:N 조인의 경우 데이터가 뻥튀기 되는 현상이 생긴다.")
    public void collectionFetchJoinTest() {
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
            memberB.setTeamRelation(teamA);
            em.persist(memberB);

            Member memberC = new Member();
            memberC.setName("memberC");
            memberC.setTeamRelation(teamB);
            em.persist(memberC);

            Member memberD = new Member();
            memberD.setName("memberD");
            em.persist(memberD);

            em.flush();
            em.clear();

            String query = "select t from com.howard.jpabasic.section11.section11_2.Team t join fetch t.members";
            List<Team> findTeams = em.createQuery(query, Team.class).getResultList();

            /* 하지만 최근 Hibernate 버전에서는 이를 별도의 명령어 없이도 자동으로 중복을 잡아준다. */
            assertThat(findTeams).hasSize(2);
            assertThat(findTeams)
                    .extracting(Team::getName)
                    .containsExactly("teamA", "teamB");

            etx.rollback();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

    /*
     * 일반 조인 vs 페치 조인
     * 1. 일반 조인은 실행 시 연관된 엔티티를 함께 조회하지 않는다.
     * 2. JPQL 은 결과를 반환할 때 연관관계를 고려하지 않음
     * 3. 단지 select 절에 지정한 entity 에 대해서만 조회한다.
     * 4. 페치 조인을 사용할 때만 연관된 엔티티도 함께 조회 (그시 로딩)
     * 5. 페치 조인은 객체 그래프를 SQL 한 번에 조회하는 개념에 해당한다.
     * */

}
