package com.howard.jpabasic.section3;

import jakarta.persistence.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@EntityScan(basePackages = {"com.howard.jpabasic.section3"})
public class Section3Test {

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
    @DisplayName("Member Entity 를 생성 후 영속화 한다.")
    public void persistMemberEntity() {
        try {
            /* Transaction start */
            etx.begin();

            /* Member 객체 생성 == 비영속 상태 */
            Member member = new Member();
            member.setUsername("ryan");
            member.setAge(25);

            /* 영속화 == 영속상태 */
            em.persist(member);

            /* 영속화된 Member Entity를 조회 */
            Member findMember = em.find(Member.class, member.getId());
            System.out.println("member.id = " + findMember.getId());
            System.out.println("member.age = " + findMember.getAge());
            System.out.println("member.username = " + findMember.getUsername());

            /* Commit Transaction */
            etx.commit();
        } catch (Exception e) {
            /* Rollback Transaction */
            etx.rollback();
        } finally {
            /* Close EntityManager */
            em.close();
        }
    }

    @Test
    @DisplayName("1차 캐시 - 영속성 컨텍스트가 1차 캐시 역할을 수행한다.")
    public void firstLevelCacheTest() {
        try {
            /* Transaction 시작 */
            etx.begin();

            /* Entity 생성 및 persist */
            Member member = new Member();
            member.setUsername("ryan");
            member.setAge(25);
            em.persist(member);

            /* 영속상태가 된 Member Entity 조회 */
            /* 1차 캐시에서 조회하므로 select 쿼리가 날아가지 않는다. */
            Member findMember = em.find(Member.class, member.getId());
            System.out.println("member.id = " + findMember.getId());
            System.out.println("member.age = " + findMember.getAge());
            System.out.println("member.username = " + findMember.getUsername());

            /* Flush 하여 Insert 쿼리를 DB에 날리고, EntityManager 를 초기화 하여 1차 캐시를 비운다. */
            System.out.println("---------------------");
            em.flush();
            em.clear();
            System.out.println("---------------------");

            /* 영속성 컨텍스트를 초기화하여 1차 캐시가 비어있으므로 조회 시 select 쿼리가 날아간다. */
            findMember = em.find(Member.class, member.getId());
            System.out.println("member.id = " + findMember.getId());
            System.out.println("member.age = " + findMember.getAge());
            System.out.println("member.username = " + findMember.getUsername());

            /* Transaction commit */
            etx.commit();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

    @Test
    @DisplayName("동일성 보장 - 동일한 Transaction 내에서는 REPEATABLE READ 등급의 격리수준을 제공한다.")
    public void identityTest() {
        try {
            /* Transaction 시작 */
            etx.begin();

            /* Entity 생성 및 persist */
            Member member = new Member();
            member.setUsername("ryan");
            member.setAge(25);
            em.persist(member);

            /* 영속상태가 된 Member Entity 조회 */
            /* 1차 캐시에서 바로 조회하는 경우 */
            Member findMemberA = em.find(Member.class, member.getId());
            Member findMemberB = em.find(Member.class, member.getId());
            System.out.println("findMemberA == findMemberB : " + (findMemberA == findMemberB));

            System.out.println("---------------------");
            em.flush();
            em.clear();
            System.out.println("---------------------");

            /* 영속상태가 된 Member Entity 조회 */
            /* DB를 거쳐서 1차 캐시에서 조회하는 경우 */
            Member findMemberC = em.find(Member.class, member.getId());
            Member findMemberD = em.find(Member.class, member.getId());
            System.out.println("findMemberC == findMemberD : " + (findMemberC == findMemberD));

            /* Transaction commit */
            etx.commit();

            /* 하지만 서로 다른 Transaction 에 대해서는 동일성을 보장하지 못한다. */
            etx.begin();
            Member findMemberE = em.find(Member.class, member.getId());
            System.out.println("findMemberA == findMemberE : " + (findMemberA == findMemberE));
            etx.commit();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

    @Test
    @DisplayName("쓰기지연 - Transaction commit 이 발생하는 순간 쓰기지연 저장소에 있던 쿼리가 한 번에 요청된다.")
    public void writeBehindTest() {
        try {
            /* Transaction 시작 */
            etx.begin();

            System.out.println("---------------------");
            /* Entity 생성 및 persist (1) */
            Member memberA = new Member();
            memberA.setUsername("ryan");
            memberA.setAge(25);
            em.persist(memberA);
            System.out.println("---------------------");

            System.out.println("---------------------");
            /* Entity 생성 및 persist (2) */
            Member memberB = new Member();
            memberB.setUsername("choonsik");
            memberB.setAge(20);
            em.persist(memberB);
            System.out.println("---------------------");

            /* Transaction commit */
            /* -> 해당 시점에 insert 쿼리가 한 번에 전송된다. */
            etx.commit();
        } catch(Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

    @Test
    @DisplayName("변경감지 - Entity에 대해서 수정이 발생한 경우, Transaction commit 시점에 update 쿼리가 전송된다.")
    public void dirtyCheckingTest() {
        try {
            /* Transaction 시작 */
            etx.begin();

            /* Entity 생성 및 persist */
            Member memberA = new Member();
            memberA.setUsername("ryan");
            memberA.setAge(25);
            em.persist(memberA);

            /* Entity 수정 */
            memberA.setUsername("Choonsik");
            memberA.setAge(20);

            /* DB에 이미 반영된 상황을 가정하기 위해 flush */
            /* Dirty Checking 에 의해 update 쿼리가 전송된다. */
            /* 이 부분을 주석처리하더라도 insert -> update 순으로 쿼리가 요청된다. */
            em.flush();
            em.clear();

            /* Transaction commit */
            etx.commit();
        } catch(Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

    @Test
    @DisplayName("flush - JPQL 쿼리를 실행하기 전에 flush가 발생한다.")
    public void flushWithJPQLTest() {
        try {
            /* Transaction 시작 */
            etx.begin();

            /* Entity 생성 및 persist (1) */
            Member memberA = new Member();
            memberA.setUsername("ryan");
            memberA.setAge(25);
            em.persist(memberA);

            /* Entity 생성 및 persist (2) */
            Member memberB = new Member();
            memberB.setUsername("choonsik");
            memberB.setAge(20);
            em.persist(memberB);

            /* JPQL 쿼리 실행 이전에 flush 가 발생한다. */
            System.out.println("-----------------------");
            List<Member> members = em
                    .createQuery("select m from Member m where m.age > 10", Member.class)
                    .getResultList();
            System.out.println("-----------------------");
            for (Member member : members) {
                System.out.println("Member.id = " + member.getId());
                System.out.println("Member.username = " + member.getUsername());
                System.out.println("Member.age = " + member.getAge());
            }

            /* Transaction commit */
            etx.commit();
        } catch(Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

    @Test
    @DisplayName("FlushModeType.AUTO - JPQL 쿼리를 실행하기 전에 flush가 발생한다.")
    public void flushModeAutoWithJPQLTest() {
        /* FlushTypeMode.AUTO 설정 */
        em.setFlushMode(FlushModeType.AUTO);
        try {
            /* Transaction 시작 */
            etx.begin();

            /* Entity 생성 및 persist (1) */
            Member memberA = new Member();
            memberA.setUsername("ryan");
            memberA.setAge(25);
            em.persist(memberA);

            /* Entity 생성 및 persist (2) */
            Member memberB = new Member();
            memberB.setUsername("choonsik");
            memberB.setAge(20);
            em.persist(memberB);

            /* JPQL 쿼리 실행 이전에 flush 가 발생한다. */
            System.out.println("-----------------------");
            List<Member> members = em
                    .createQuery("select m from Member m where m.age > 10", Member.class)
                    .getResultList();
            System.out.println("-----------------------");
            for (Member member : members) {
                System.out.println("Member.id = " + member.getId());
                System.out.println("Member.username = " + member.getUsername());
                System.out.println("Member.age = " + member.getAge());
            }

            /* Transaction commit */
            etx.commit();
        } catch(Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

    @Test
    @DisplayName("FlushModeType.COMMIT - JPQL 쿼리를 실행하기 전에 flush 가 발생하지 않고, Commit 시 발생한다.")
    public void flushModeCommitWithJPQLTest() {
        /* FlushTypeMode.AUTO 설정 */
        em.setFlushMode(FlushModeType.COMMIT);
        try {
            /* Transaction 시작 */
            etx.begin();

            /* Entity 생성 및 persist (1) */
            Member memberA = new Member();
            memberA.setUsername("ryan");
            memberA.setAge(25);
            em.persist(memberA);

            /* Entity 생성 및 persist (2) */
            Member memberB = new Member();
            memberB.setUsername("choonsik");
            memberB.setAge(20);
            em.persist(memberB);

            /* JPQL 쿼리 실행 이전에 flush 가 발생한다. */
            System.out.println("-----------------------");
            List<Member> members = em
                    .createQuery("select m from Member m where m.age > 10", Member.class)
                    .getResultList();
            System.out.println("-----------------------");
            for (Member member : members) {
                System.out.println("Member.id = " + member.getId());
                System.out.println("Member.username = " + member.getUsername());
                System.out.println("Member.age = " + member.getAge());
            }

            /* Transaction commit */
            etx.commit();
        } catch(Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

}
