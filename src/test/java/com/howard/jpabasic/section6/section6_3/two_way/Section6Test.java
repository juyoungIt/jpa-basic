package com.howard.jpabasic.section6.section6_3.two_way;

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
@EntityScan(basePackages = {"com.howard.jpabasic.section6.section6_3.two_way"})
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
    @DisplayName("1:1 양방향 - 양방향 연관관계를 가지는 Entity 생성 및 저장, 조회")
    public void twoWayOneToOneRelationTest() {
        try {
            etx.begin();

            /* Locker Entity 생성 및 영속화 */
            Locker locker = new Locker();
            locker.setName("lockerA");
            em.persist(locker);

            /* Member Entity 생성 및 영속화 */
            Member member = new Member();
            member.setUsername("ryan");
            member.setLockerRelation(locker); // 유틸리티 메서드를 활용하여 연관관계 설정 처리
            em.persist(member);

            /* select query 를 확인하기 위한 목적으로 추가 */
            em.flush();
            em.clear();

            /* 주 테이블에 외래키가 존재하는 경우 */
            /* -> 주 객체가 대상 객체의 참조를 가지는 것처럼 주 테이블에 외래키를 두고 대상 테이블을 찾음 */
            /* -> 객체지향 개발자가 선호하는 방식 */
            /* -> JPA 매핑이 편리함 */
            /* -> 장점 : 주 테이블만 조회해도 대상 테이블에 데이터가 있는 지 확인이 가능함 */
            /* -> 단점 : 값이 없는 경우 외래키에 null 값을 허용하게 됨 */

            /* 대상 테이블에 외래키가 존재하는 경우 */
            /* -> DBA 가 선호하는 방식 */
            /* -> 장점 : 주 테이블과 대상 테이블을 일대일 관계에서 일대다 관계로 변경할 때 테이블 구조를 유지할 수 있음 */
            /* -> 단점 : 프록시 기능의 한계로 인해 지연로딩으로 설정해도 항상 즉시로딩됨 (세부 내용은 프록시에서 정리) */

            /* 단방향 연관관계를 가지는 Member Entity 조회 */
            Member findMember = em.find(Member.class, member.getId());
            System.out.println("findMember.id = " + findMember.getId());
            System.out.println("findMember.username = " + findMember.getUsername());
            System.out.println("findMember.locker.id = " + findMember.getLocker().getId());
            System.out.println("findMember.locker.name = " + findMember.getLocker().getName());
            System.out.println("findMember.locker.member.id = " + findMember.getLocker().getMember().getId());
            System.out.println("findMember.locker.member.username = " + findMember.getLocker().getMember().getUsername());

            etx.commit();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

}
