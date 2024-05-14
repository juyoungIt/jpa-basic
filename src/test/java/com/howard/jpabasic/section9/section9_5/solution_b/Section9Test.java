package com.howard.jpabasic.section9.section9_5.solution_b;

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
@EntityScan(basePackages = {"com.howard.jpabasic.section9.section9_5.solution_b"})
public class Section9Test {

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
    @DisplayName("값 타입 컬렉션 - 값 타입 컬렉션을 다대일 양방향 연관관계로 풀어 Entity 승격을 통해 문제를 해결한다.")
    public void valueTypeCollectionToManyToOneRelationTest() {
        try {
            etx.begin();

            /* Member Entity 생성 및 영속화 */
            Member member = new Member();
            member.setName("ryan");
            member.setName("ryan");
            member.setHomeAddress(new Address("homeCity", "homeStreet", "homeZipcode"));
            member.getFavoriteFoods().add("seafood");
            member.getFavoriteFoods().add("pork");
            member.getFavoriteFoods().add("beef");
            em.persist(member);

            /* 변경된 부분 */
            AddressEntity historyA = new AddressEntity();
            historyA.setAddress(new Address("prevCity", "prevStreet", "prevZipcode"));
            historyA.setMemberRelation(member);
            em.persist(historyA);

            AddressEntity historyB = new AddressEntity();
            historyB.setAddress(new Address("homeCity", "homeStreet", "homeZipcode"));
            historyB.setMemberRelation(member);
            em.persist(historyB);

            /* select query 를 확인하기 위한 목적으로 작성한 코드 */
            em.flush();
            em.clear();

            /* 영속화한 Member Entity 조회 */
            Member findMember = em.find(Member.class, member.getId());
            System.out.println("findMember.id = " + findMember.getId());
            System.out.println("findMember.name = " + findMember.getName());
            System.out.println("findMember.homeAddress = " + findMember.getHomeAddress());
            System.out.println("findMember.favoriteFoods = " + findMember.getFavoriteFoods());
            System.out.println("findMember.addressHistory = " + findMember.getAddressHistory());

            etx.commit();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

}
