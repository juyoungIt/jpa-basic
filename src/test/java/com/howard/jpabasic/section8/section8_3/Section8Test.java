package com.howard.jpabasic.section8.section8_3;

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
@EntityScan(basePackages = {"com.howard.jpabasic.section8.section8_3"})
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

    /*
     * 영속성 전이
     * 1. 특정 엔티티를 영속 상태를 만들 때 연관된 엔티티도 함께 영속 상태로 만들고 싶을 때
     * 2. 영속성 전이는 연관관계를 매핑하는 것과 아무 관련이 없음
     * 3. 엔티티를 영속화 할 때 연관된 엔티티도 함께 영속화 하도록 하는 편리함을 제공하는 것이다.
     *
     * CASCADE 의 종류
     * 1. ALL : 모두 적용
     * 2. PERSIST : 영속
     * 3. REMOVE : 삭제
     * 4. MERGE : 병합
     * 5. REFRESH : REFRESH
     * 6. DETACH : DETACH
     * -> 실무에서 이 옵션들을 막 딥하게 사용하지는 않는다.
     *
     * */

    @Test
    @DisplayName("CascadeType - ")
    public void cascadeTest() {
        try {
            etx.begin();

            /* Parent Entity 생성 및 영속화 */
            Parent parent = new Parent();
            parent.setName("parentA");
            em.persist(parent);

            /* Children Entity 생성 */
            /* -> CASCADE 설정을 하니 직접 persist 하지 않았지만 children 이 persist 됨을 확인할 수 있다. */
            Children childrenA = new Children();
            childrenA.setName("childrenA");
            childrenA.setParentRelation(parent); // 연관관계 주입 유틸리티 메서드 사용 (1)

            Children childrenB = new Children();
            childrenB.setName("childrenB");
            childrenB.setParentRelation(parent); // 연관관계 주입 유틸리티 메서드 사용 (2)

            Children childrenC = new Children();
            childrenC.setName("childrenC");
            childrenC.setParentRelation(parent); // 연관관계 주입 유틸리티 메서드 사용 (3)

            etx.commit();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

    /*
     * 고아 객체 제거
     * 1. 부모 엔티티와 연관관계가 끊어진 자식 엔티티를 자동으로 삭제
     * 2. orphanRemoval = true
     *
     * 주의할 점!
     * 1. 참조가 제거된 엔티티는 다른 곳에서 참조하지 않는 고아객체로 보고 삭제하는 기능
     * 2. 즉, 참조하는 곳이 한 곳일 때만 사용해야 함! -> 특정 엔티티가 개인소유 하는 경우만 가능
     * 3. @OneToOne, @OneToMany 만 가능
     * 4. 참고 : 개념적으로 부모를 제거하면 자식은 고아가 된다.
     *          따라서 고아객체 제거 기능을 활성화하면, 부모를 제거할 때 자식도 함께 제거된다.
     *          이것은 마치 CascadeType.REMOVE 처럼 동작한다.
     * 5. CascadeType.ALL + orphanRemoval = true 는 어떤 의미를 가질 까?
     *    * 스스로 생명주기를 관리하는 엔티티는 em.persist() 로 영속화, em.remove() 로 제거
     *    * 두 옵션을 모두 활성화하면 부모 엔티티를 통해서 자식의 생명 주기를 관리할 수 있음. -> DAO, Repository 가 없어도 됨
     *    * 도메인 주도 설계 (DDD)의 Aggregate Root 개념을 구현할 때 유용함
     * */

    @Test
    @DisplayName("고아객체 삭제 - 부모 엔티티와 연관관계가 끊어진 자식 엔티티를 자동으로 삭제한다.")
    public void orphanRemovalTest() {
        try {
            etx.begin();

            /* Parent Entity 생성 및 영속화 */
            Parent parent = new Parent();
            parent.setName("parentA");
            em.persist(parent);

            /* Children Entity 생성 */
            /* -> CASCADE 설정을 하니 직접 persist 하지 않았지만 children 이 persist 됨을 확인할 수 있다. */
            Children childrenA = new Children();
            childrenA.setName("childrenA");
            childrenA.setParentRelation(parent); // 연관관계 주입 유틸리티 메서드 사용 (1)

            Children childrenB = new Children();
            childrenB.setName("childrenB");
            childrenB.setParentRelation(parent); // 연관관계 주입 유틸리티 메서드 사용 (2)

            Children childrenC = new Children();
            childrenC.setName("childrenC");
            childrenC.setParentRelation(parent); // 연관관계 주입 유틸리티 메서드 사용 (3)

            /* select query 를 확인하기 위한 목적으로 추가한 코드 */
            em.flush();
            em.clear();

            Parent findParent = em.find(Parent.class, parent.getId());
            /* 고아객체를 제거하는 매너니즘이 동작하여 delete query 가 날아간다. */
            findParent.getChildList().remove(0);

            etx.commit();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

}
