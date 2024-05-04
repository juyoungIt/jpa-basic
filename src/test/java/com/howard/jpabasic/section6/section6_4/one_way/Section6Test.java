package com.howard.jpabasic.section6.section6_4.one_way;

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
@EntityScan(basePackages = {"com.howard.jpabasic.section6.section6_4.one_way"})
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
    @DisplayName("N:N 단방향 - 단방향 연관관계를 가지는 Entity 생성 및 저장, 조회")
    public void oneWayManyToManyRelationTest() {
        try {
            etx.begin();

            /* Product Entity 생성 및 영속화 */
            Product productA = new Product();
            productA.setName("productA");
            em.persist(productA);

            Product productB = new Product();
            productB.setName("productB");
            em.persist(productB);

            /* Member Entity 생성 및 영속화 */
            Member memberA = new Member();
            memberA.setName("ryan");
            memberA.getProducts().add(productA);
            memberA.getProducts().add(productB);
            em.persist(memberA);

            Member memberB = new Member();
            memberB.setName("choonsik");
            memberB.getProducts().add(productA);
            memberB.getProducts().add(productB);
            em.persist(memberB);

            /* select query 를 확인하기 위한 용도로 작성한 코드 */
            em.flush();
            em.clear();

            /* 편리해 보이지만 실무에서 사용을 권장하지 않는다. */
            /* -> 중간 테이블을 Entity 로 승격시켜서 1:N & N:1 관계로 풀어내는 방식을 권장한다. */

            /* 단방향 연관관계를 가지는 Entity 조회 */
            Member findMemberA = em.find(Member.class, memberA.getId());
            System.out.println("findMemberA.id = " + findMemberA.getId());
            System.out.println("findMemberA.name = " + findMemberA.getName());
            System.out.println("findMemberA.products = " + findMemberA.getProducts());
            Member findMemberB = em.find(Member.class, memberB.getId());
            System.out.println("findMemberB.id = " + findMemberB.getId());
            System.out.println("findMemberB.name = " + findMemberB.getName());
            System.out.println("findMemberB.products = " + findMemberB.getProducts());

            etx.commit();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

}
