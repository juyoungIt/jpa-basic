package com.howard.jpabasic.section9.section9_5.problem;

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
@EntityScan(basePackages = {"com.howard.jpabasic.section9.section9_5.problem"})
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
    @DisplayName("값 타입 컬렉션 - 컬렉션도 서로 다른 Table 로 매핑하여 그 정보를 관리할 수 있다.")
    public void valueTypeCollectionTest() {
        try {
            etx.begin();

            /* Member Entity 생성 및 영속화 */
            Member member = new Member();
            member.setName("ryan");
            member.setHomeAddress(new Address("homeCity", "homeAddress", "homeZipcode"));

            /* 해당 부분으로 인해 3개의 insert query 가 분리된 Table 로 날아감 */
            /* -> 따로 persist 하지 않아도 분리된 다른 Table 에 함께 저장된다 */
            /* -> 라이프 사이클이 포함되는 Entity 에 의존하는 관계가 된다. */
            /* -> 즉, 영속성 전이(CASCADE) + 고아객체 제거기능을 필수라고 가지게 된다 */
            member.getFavoriteFoods().add("seafood");
            member.getFavoriteFoods().add("pork");
            member.getFavoriteFoods().add("beef");

            /* 해당 부분으로 인해 2개의 insert query 가 분리된 Table 로 날아감 */
            /* -> 따로 persist 하지 않아도 분리된 다른 Table 에 함께 저장된다 */
            /* -> 라이프 사이클이 포함되는 Entity 에 의존하는 관계가 된다. */
            /* -> 즉, 영속성 전이(CASCADE) + 고아객체 제거기능을 필수라고 가지게 된다 */
            member.getAddressHistory().add(new Address("prevCity", "prevAddress", "prevZipcode"));
            member.getAddressHistory().add(new Address("homeCity", "homeAddress", "homeZipcode"));

            em.persist(member);

            /* select query 를 확인하기 위한 목적으로 추가한 코드 */
            em.flush();
            em.clear();

            /* 영속화한 Member Entity 를 조회 */
            System.out.println("===== First Request =====");
            Member findMember = em.find(Member.class, member.getId()); // 값 컬렉션 타입은 기본적으로 지연로딩 전략을 사용
            System.out.println("findMember.id = " + findMember.getId());
            System.out.println("findMember.name = " + findMember.getName());
            System.out.println("findMember.homeAddress = " + findMember.getHomeAddress());

            System.out.println("===== Request To Collection1 =====");
            /* 지연 로딩 전략을 사용하므로 해당 시점에 추가적인 조회 쿼리가 나간다 */
            System.out.println("findMember.favoriteFoods = " + findMember.getFavoriteFoods());

            System.out.println("===== Request To Collection2 =====");
            /* 지연 로딩 전략을 사용하므로 해당 시점에 추가적인 조회 쿼리가 나간다 */
            System.out.println("findMember.addressHistory = " + findMember.getAddressHistory());

            /* 영속화한 Member Entity 의 속성값을 수정 */
            /* -> 값 타입은 immutable 해야하므로 setter 를 사용한 변경을 권장하지 않는다. */

            /* 다음 수정하는 예시들을 잘 기억해두자 */
            /* -> 1. 값 타입 변경 -> homeAddress 에서 homeCity -> newCity 로 변경 */
            findMember.setHomeAddress(new Address(
                    "newCity",
                    findMember.getHomeAddress().getStreet(),
                    findMember.getHomeAddress().getZipcode()));

            /* -> 2. Set 컬렉션 변경 -> favoriteFoods 에서 beef -> lamb 로 변경 */
            /*    -> 수정한다고 생각할 수 있지만 컬렉션에 포함되는 기본값 String 을 새것으로 교체하는 느낌으로 이해하면 된다. */
            findMember.getFavoriteFoods().remove("beef");
            findMember.getFavoriteFoods().add("lamb");

            /* -> 3. List 컬렉션 변경 -> addressHistory 에서 prevCity -> new City 로 변경 */
            // 컬렉션의 remove 과정에서 equals() 를 사용하는 경우가 많음 -> 그만큼 equals() 의 적절한 정의가 중요!
            /* 집중! -> 다음 코드를 실행하면 예상과 달리 1개의 delete query, 2개의 insert query 가 나간다? */
            /*      -> 그 이유는 값 타입 컬렉션의 제약사항을 이해해야 알 수 있다. Member Entity 에 작성한 주석을 함께 참고하자. */
            findMember.getAddressHistory().remove(new Address("prevCity", "prevAddress", "prevZipcode"));
            findMember.getAddressHistory().add(new Address("newCity", "prevAddress", "prevAddress"));

            etx.commit();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

}
