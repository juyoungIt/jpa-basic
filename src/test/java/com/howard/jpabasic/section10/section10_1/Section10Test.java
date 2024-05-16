package com.howard.jpabasic.section10.section10_1;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceUnit;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@EntityScan(basePackages = {"com.howard.jpabasic.section10.section10_1"})
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
    @DisplayName("JPQL Basic Example - Member 중에서 이름이 'ryanha' 인 Member 를 조회한다.")
    public void findMemberWhereNameContainRyan() {
        try {
            etx.begin();

            /* Member Entity 생성 및 영속화 */
            Member memberA = new Member();
            memberA.setName("ryanha");
            em.persist(memberA);
            Member memberB = new Member();
            memberB.setName("choonsik");
            em.persist(memberB);
            Member memberC = new Member();
            memberC.setName("prodo");
            em.persist(memberC);
            Member memberD = new Member();
            memberD.setName("muzy");
            em.persist(memberD);
            Member memberE = new Member();
            memberE.setName("ryanChoon");
            em.persist(memberE);

            /* 이름에 ryan 을 포함하는 Member 를 검색하는 JPQL Query 를 작성 */
            Member findMember = em
                    .createQuery("select m from Member m where m.name = 'ryanha'", Member.class)
                    .getSingleResult();

            /* 조회된 사용자 정보를 출력 */
            System.out.println("findMember.id = " + findMember.getId());
            System.out.println("findMember.username = " + findMember.getName());

            etx.commit();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.clear();
        }
    }

    @Test
    @DisplayName("JPA Criteria - JPA 공식 기능인 JPA Criteria 를 활용한다.")
    public void jpaCriteriaTest() {
        try {
            etx.begin();

            /* Member Entity 생성 및 영속화 */
            Member memberA = new Member();
            memberA.setName("ryanha");
            em.persist(memberA);
            Member memberB = new Member();
            memberB.setName("choonsik");
            em.persist(memberB);
            Member memberC = new Member();
            memberC.setName("prodo");
            em.persist(memberC);
            Member memberD = new Member();
            memberD.setName("muzy");
            em.persist(memberD);
            Member memberE = new Member();
            memberE.setName("ryanChoon");
            em.persist(memberE);

            /*
             * JPA Criteria 특징
             * 1. 문자가 아닌 자바코드로 SQL 을 작성할 수 있음
             * 2. JPQL 빌더 역할
             * 3. JPA 공식 기능
             * 4. 하지만 너무 복잡하고 실용성이 없다.
             * 5. Criteria 대신에 Querydsl 의 사용을 권장
             *  */

            /* criteria 사용 준비 */
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Member> query = cb.createQuery(Member.class);

            Root<Member> member = query.from(Member.class);
            CriteriaQuery<Member> cq = query.select(member).where(cb.equal(member.get("name"), "ryanha"));
            List<Member> findMembers = em.createQuery(cq).getResultList();

            /* 조회된 사용자 정보를 출력 */
            for (Member findMember : findMembers) {
                System.out.println("findMember.id = " + findMember.getId());
                System.out.println("findMember.name = " + findMember.getName());
            }

            etx.commit();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

    @Test
    @DisplayName("NativeQuery - 직접 SQL 을 작성하여 요청한다.")
    public void nativeQueryTest() {
        try {
            etx.begin();

            /* Member Entity 생성 및 영속화 */
            Member memberA = new Member();
            memberA.setName("ryanha");
            em.persist(memberA);
            Member memberB = new Member();
            memberB.setName("choonsik");
            em.persist(memberB);
            Member memberC = new Member();
            memberC.setName("prodo");
            em.persist(memberC);
            Member memberD = new Member();
            memberD.setName("muzy");
            em.persist(memberD);
            Member memberE = new Member();
            memberE.setName("ryanChoon");
            em.persist(memberE);

            /* Native SQL
             * 1. JPA 가 제공하는 SQL을 직접 사용하는 기능
             * 2. JPQL 로 해결할 수 없는 특정 데이터베이스에 의존적인 기능
             * -> 특정 DB 에서만 사용할 수 있는 SQL 힌트를 사용하고 싶은 경우 고려 가능
             * -> 물론 이 외에도 다른 방법을 사용하여 특정 DB 에서만 사용 가능한 SQL 힌트를 사용 가능
             * */
            String nativeQuery = "select * from member as m where m.name = 'ryanha'";
            Member findMember = (Member) em.createNativeQuery(nativeQuery, Member.class).getSingleResult();
            System.out.println("findMember.id = " + findMember.getId());
            System.out.println("findMember.name = " + findMember.getName());

            etx.commit();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

}
