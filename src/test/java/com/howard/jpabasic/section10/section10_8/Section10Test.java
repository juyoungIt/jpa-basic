package com.howard.jpabasic.section10.section10_8;

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

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@EntityScan(basePackages = {"com.howard.jpabasic.section10.section10_8"})
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
    @DisplayName("기본 CASE 식 - 나이 10살 이하는 '학생요금' 60세 이상은 '경로요금' 나머지는 '일반요금'을 출력한다")
    public void basicCaseStatementTest() {
        try {
            etx.begin();

            Member memberA = new Member();
            memberA.setUsername("memberA");
            memberA.setAge(10);
            em.persist(memberA);

            Member memberB = new Member();
            memberB.setUsername("memberB");
            memberB.setAge(27);
            em.persist(memberB);

            Member memberC = new Member();
            memberC.setUsername("memberC");
            memberC.setAge(60);
            em.persist(memberC);

            String query = "select " +
                           "  case" +
                           "    when m.age <= 10 then '학생요금'" +
                           "    when m.age >= 60 then '경로요금'" +
                           "    else '일반요금'" +
                           "  end " +
                           "from com.howard.jpabasic.section10.section10_8.Member m ";
            List<String> fees = em.createQuery(query, String.class).getResultList();

            assertThat(fees).containsExactly("학생요금", "일반요금", "경로요금");

            etx.rollback();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

    @Test
    @DisplayName("단순 CASE 식 - teamA 에게는 인센티브 110%, teamB 에게는 인센티브 120%, 나머지 팀에게는 인센티브 105% 를 부여한다.")
    public void simpleCaseStatementTest() {
        try {
            etx.begin();

            Team teamA = new Team();
            teamA.setName("teamA");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("teamB");
            em.persist(teamB);

            Team teamC = new Team();
            teamC.setName("teamC");
            em.persist(teamC);

            Member memberA = new Member();
            memberA.setUsername("memberA");
            memberA.setAge(10);
            memberA.setTeamRelation(teamA);
            em.persist(memberA);

            Member memberB = new Member();
            memberB.setUsername("memberB");
            memberB.setAge(27);
            memberB.setTeamRelation(teamB);
            em.persist(memberB);

            Member memberC = new Member();
            memberC.setUsername("memberC");
            memberC.setAge(60);
            memberC.setTeamRelation(teamC);
            em.persist(memberC);

            String query = "select " +
                           "  case t.name " +
                           "    when 'teamA' then '인센티브 110%'" +
                           "    when 'teamB' then '인센티브 120%'" +
                           "    else '인센티브 105%'" +
                           "  end " +
                           "from com.howard.jpabasic.section10.section10_8.Member m join m.team t";
            List<String> results = em.createQuery(query, String.class).getResultList();

            assertThat(results).containsExactly("인센티브 110%", "인센티브 120%", "인센티브 105%");

            etx.rollback();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

    @Test
    @DisplayName("coalesce() - 사용자 이름이 없는 경우 '이름없음' 을 반환한다")
    public void coalesceTest() {
        try {
            etx.begin();

            Member memberA = new Member();
            memberA.setUsername("memberA");
            em.persist(memberA);

            Member memberB = new Member();
            memberB.setUsername(null);
            em.persist(memberB);

            Member memberC = new Member();
            memberC.setUsername("memberC");
            em.persist(memberC);

            String query = "select coalesce(m.username, '이름없음') from com.howard.jpabasic.section10.section10_8.Member m";
            List<String> results = em.createQuery(query, String.class).getResultList();

            assertThat(results).containsExactly("memberA", "이름없음", "memberC");

            etx.rollback();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

    @Test
    @DisplayName("nullif() - 사용자 일므이 admin 인 경우 그 이름을 null 로 반환한다")
    public void nullifTest() {
        try {
            etx.begin();

            Member memberA = new Member();
            memberA.setUsername("admin");
            em.persist(memberA);

            Member memberB = new Member();
            memberB.setUsername("memberB");
            em.persist(memberB);

            Member memberC = new Member();
            memberC.setUsername("memberC");
            em.persist(memberC);

            String query = "select nullif(m.username, 'admin') from com.howard.jpabasic.section10.section10_8.Member m";
            List<String> results = em.createQuery(query, String.class).getResultList();

            assertThat(results).containsExactly(null, "memberB", "memberC");

            etx.rollback();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

}
