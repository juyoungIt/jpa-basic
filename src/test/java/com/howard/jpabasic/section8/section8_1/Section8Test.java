package com.howard.jpabasic.section8.section8_1;

import jakarta.persistence.*;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
@EntityScan(basePackages = {"com.howard.jpabasic.section8.section8_1"})
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
    @DisplayName("프록시 확인 - 프록시 클래스 확인 방법")
    public void checkProxyClassTest() {
        try {
            etx.begin();

            /* Member Entity 생성 및 영속화 */
            Member member = new Member();
            member.setUsername("ryan");
            member.setAge(25);
            member.setCreateAt(LocalDateTime.now());
            member.setLastModifiedAt(LocalDateTime.now());
            em.persist(member);

            /* 영속화한 Entity 를 DB에 반영하기 위한 코드 */
            em.flush();
            em.clear();

            Member referenceMember = em.getReference(Member.class, member.getId());
            System.out.println("referenceMember = " + referenceMember.getClass().getName());

            etx.commit();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

    @Test
    @DisplayName("프록시 확인 - 프록시 인스턴스의 초기화 여부 확인")
    public void checkProxyInitializeTest() {
        try {
            etx.begin();

            /* Member Entity 생성 및 영속화 */
            Member member = new Member();
            member.setUsername("ryan");
            member.setAge(25);
            member.setCreateAt(LocalDateTime.now());
            member.setLastModifiedAt(LocalDateTime.now());
            em.persist(member);

            /* 영속화한 Entity 를 DB에 반영하기 위한 코드 */
            em.flush();
            em.clear();

            PersistenceUnitUtil persistenceUnitUtil = emf.getPersistenceUnitUtil();
            Member referenceMember = em.getReference(Member.class, member.getId());
            System.out.println("before : referenceMember.isLoaded = " + persistenceUnitUtil.isLoaded(referenceMember));
            Hibernate.initialize(referenceMember); // 프록시 객체를 강제로 초기화
            System.out.println("after : referenceMember.isLoaded = " + persistenceUnitUtil.isLoaded(referenceMember));

            etx.commit();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

    /* em.find() : 데이터베이스를 통해서 실제 엔티티 객체를 조회 */
    /* em.gerReference() : 데이터베이스 조회를 미루는 가짜(프록시) 엔티티 객체 조회 */
    /* 프록시의 특징
     * 1. 실제 클래스를 상속 받아서 만들어짐
     * 2. 실제 클래스와 겉 모양이 같다.
     * 3. 사용하는 입장에서는 진짜 객체인지 프록시 객체인지 구분하지 않고 사용하면 됨 (이론상)
     * 4. 프록시 객체는 실제 객체의 참조(target)를 보관
     * 5. 프록시 객체를 호출하면 프록시 객체는 실제 객체의 메소드 호출
     * 6. 프록시 객체는 처음 사용할 때 한 번만 초기화 된다.
     * 7. 프록시 객체 초기화 시 프록시 객체가 실제 엔티티로 변경되는 것이 아님. -> 프록시 객체를 통해서 실제 엔티티에 접근이 가능해지는 것.
     * 8. 프록시 객체는 원본 엔티티를 상속받는다. -> 타입 체크 시 주의해야 한다. (== 대신 instanceof 를 사용해야 한다.)
     * 9. 영속성 컨텍스트에 찾는 엔티티가 이미 있는 경우 -> em.gerReference()를 호출해도 실제 엔티티를 반환한다.
     * 10. 한번 프록시가 조회가 되었다면, em.find() 도 엔티티가 아닌 프록시를 반환할 수 있다.
     * 11. 영속성 컨텍스트의 도움을 받을 수 없는 준영속 상태인 경우, 프록시를 초기화하면 예외가 발생한다.
     *  */

    @Test
    @DisplayName("em.find() vs em.gerReference() 의 동작에는 차이가 있다.")
    public void gerReferenceTest() {
        try {
            etx.begin();

            /* Member Entity 생성 및 영속화 */
            Member member = new Member();
            member.setUsername("ryan");
            member.setAge(25);
            member.setCreateAt(LocalDateTime.now());
            member.setLastModifiedAt(LocalDateTime.now());
            em.persist(member);

            /* select query 를 확인하기 위한 목적으로 추가한 코드 */
            em.flush();
            em.clear();

            /* em.find() 를 이용하여 Member 를 조회 */
            Member findMember = em.find(Member.class, member.getId());
            System.out.println("findMember.id = " + findMember.getId());
            System.out.println("findMember.username = " + findMember.getUsername());
            System.out.println("findMember.age = " + findMember.getAge());
            System.out.println("findMember.createAt = " + findMember.getCreateAt());
            System.out.println("findMember.lastModifiedAt = " + findMember.getLastModifiedAt());

            /* 1차 캐시를 비우기 위한 목적으로 작성한 코드 */
            em.clear();

            /* em.gerReference() 를 이용하여 Member 를 조회 */
            Member referenceMember = em.getReference(Member.class, member.getId());
            System.out.println("referenceMember.id = " + referenceMember.getId());
            /* getReference() 를 호출하는 시점이 아닌 실질적인 데이터를 요청하는 시점에 select query 가 날아간다. */
            /* -> 이 시점에 프록시 객체 초기화가 발생하게 된다. */
            System.out.println("------- init proxy object -------");
            System.out.println("referenceMember.username = " + referenceMember.getUsername());
            System.out.println("referenceMember.age = " + referenceMember.getAge());
            System.out.println("referenceMember.createAt = " + referenceMember.getCreateAt());
            System.out.println("referenceMember.lastModifiedAt = " + referenceMember.getLastModifiedAt());

            etx.commit();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

    @Test
    @DisplayName("프록시 객체 초기화 시 프록시 객체가 실제 엔티티로 변경되는 것이 아님. -> 프록시 객체를 통해서 실제 엔티티에 접근이 가능")
    public void proxyTestA() {
        try {
            etx.begin();

            /* Member Entity 생성 및 영속화 */
            Member member = new Member();
            member.setUsername("ryan");
            member.setAge(25);
            member.setCreateAt(LocalDateTime.now());
            member.setLastModifiedAt(LocalDateTime.now());
            em.persist(member);

            /* select query 를 확인하기 위한 목적으로 추가한 코드 */
            em.flush();
            em.clear();

            Member referenceMember = em.getReference(Member.class, member.getId());
            System.out.println("before init = " + referenceMember.getClass().getName());
            Hibernate.initialize(referenceMember);
            /* 초기화 후에도 여전히 프록시 객체이지 엔티티로 교체된 것이 아니다 */
            System.out.println("after init = " + referenceMember.getClass().getName());

            etx.commit();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

    @Test
    @DisplayName("프록시 객체는 원본 엔티티를 상속받는다. -> 타입 체크 시 주의해야 한다. (== 대신 instanceof 를 사용해야 한다.)")
    public void proxyTestB() {
        try {
            etx.begin();

            /* Member Entity 생성 및 영속화 */
            Member memberA = new Member();
            memberA.setUsername("ryan");
            memberA.setAge(25);
            memberA.setCreateAt(LocalDateTime.now());
            memberA.setLastModifiedAt(LocalDateTime.now());
            em.persist(memberA);

            Member memberB = new Member();
            memberB.setUsername("choonsik");
            memberB.setAge(20);
            memberB.setCreateAt(LocalDateTime.now());
            memberB.setLastModifiedAt(LocalDateTime.now());
            em.persist(memberB);

            /* 영속화한 Entity 들을 미리 DB 와 동기화 하고, 1차 캐시를 비우기 위한 목적으로 작성한 코드 */
            em.flush();
            em.clear();

            Member findMemberA = em.find(Member.class, memberA.getId()); // Entity 를 반환
            Member findMemberB = em.getReference(Member.class, memberB.getId()); // Proxy 를 반환
            /* 주의할 코드! => 이런식으로 타입 비교를 하면 원인 파악이 어려운 오류에 빠질 수 있다. */
            System.out.println("findMemberA == findMemberB : " + (findMemberA.getClass() == findMemberB.getClass()));
            /* 대안 코드! => instanceof 를 사용해서 원본 Entity와 Proxy가 비교되더라도 올바르게 처리되도록 하자 */
            System.out.println("findMemberA == findMemberB : " + (findMemberA.getClass().isInstance(findMemberB)));

            etx.commit();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

    @Test
    @DisplayName("영속성 컨텍스트에 찾는 엔티티가 이미 있는 경우 -> em.gerReference()를 호출해도 실제 엔티티를 반환한다.")
    public void proxyTestC() {
        try {
            etx.begin();

            /* Member Entity 생성 및 영속화 */
            Member member = new Member();
            member.setUsername("ryan");
            member.setAge(25);
            member.setCreateAt(LocalDateTime.now());
            member.setLastModifiedAt(LocalDateTime.now());
            em.persist(member);

            /* 영속성 컨텍스트에 Member Entity 가 이미 존재하므로 Member Entity 를 반환한다. */
            Member findMember = em.getReference(Member.class, member.getId());
            System.out.println("referenceMember = " + findMember.getClass().getName());

            /* 영속화 Entity DB 동기화 및 영속성 컨텍스트 초기화 */
            em.flush();
            em.clear();

            /* 영속성 컨텍스트에 Member Entity 가 없으므로 Member Proxy Entity 를 반환한다. */
            Member referenceMember = em.getReference(Member.class, member.getId());
            System.out.println("referenceMember = " + referenceMember.getClass().getName());

            etx.commit();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

    @Test
    @DisplayName("한번 프록시가 조회가 되었다면, em.find() 도 엔티티가 아닌 프록시를 반환할 수 있다. -> 영속성 컨테스트의 동일성 보장을 위함")
    public void proxyTestD() {
        try {
            etx.begin();

            /* Member Entity 생성 및 영속화 */
            Member member = new Member();
            member.setUsername("ryan");
            member.setAge(25);
            member.setCreateAt(LocalDateTime.now());
            member.setLastModifiedAt(LocalDateTime.now());
            em.persist(member);

            /* 영속화 Entity DB 동기화 및 영속성 컨텍스트 초기화 */
            em.flush();
            em.clear();

            /* 영속성 컨텍스트에 Member Entity 가 없으므로 Member Proxy Entity 를 반환한다. */
            Member referenceMember = em.getReference(Member.class, member.getId());
            System.out.println("referenceMember = " + referenceMember.getClass().getName());

            /* 한번 Member Proxy 객체가 조회되었기 때문에 em.find() 도 Member Proxy 를 반환한다. */
            Member findMember = em.getReference(Member.class, member.getId());
            System.out.println("referenceMember = " + findMember.getClass().getName());

            etx.commit();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

}
