package com.howard.jpabasic.section9.section9_3;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@EntityScan(basePackages = {"com.howard.jpabasic.section9.section9_3"})
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
    @DisplayName("임베디드 타입 같은 값 타입을 여러 엔티티에서 공유하면 위험하다 -> 예상 및 디버깅이 어려운 side-effect 가 발생한다")
    public void sharingEmbeddedTypeTest() {
        try {
            etx.begin();

            /* Address EmbeddedType Value 생성 */
            Address address = new Address("oldCity", "oldStreetA", "oldZipcodeA");

            /* Member Entity 생성 및 영속화 */
            Member memberA = new Member();
            memberA.setName("ryan");
            memberA.setWorkPeriod(new Period(
                    LocalDateTime.of(2023, 2, 1, 10, 0, 0),
                    LocalDateTime.now()
            ));
            memberA.setHomeAddress(address);
            em.persist(memberA);

            Member memberB = new Member();
            memberB.setName("choonsik");
            memberB.setWorkPeriod(new Period(
                    LocalDateTime.of(2024, 2, 1, 10, 0, 0),
                    LocalDateTime.now()
            ));
            memberB.setHomeAddress(address); // 임베디드 값 타입을 공유하게 되는 지점
            em.persist(memberB);

            /* memberA의 주소값에서 유편번호를 새로운 주소값으로 변경하고 싶은 상황 */
            Member tmpMemberA = em.find(Member.class, memberA.getId());
            tmpMemberA.getHomeAddress().setZipcode("newZipCode");

            /* update query 를 확인하기 위한 목적으로 작성한 코드 */
            em.flush(); // 해당 시점에 update query 가 2번 나가게 된다. -> 의도하지 않은 동작이 발생
            em.clear();

            /* 영속화한 Member Entity 조회 */
            Member findMemberA = em.find(Member.class, memberA.getId());
            Member findMemberB = em.find(Member.class, memberB.getId());

            assertThat(findMemberA.getName()).isEqualTo("ryan");
            assertThat(findMemberB.getName()).isEqualTo("choonsik");
            /* MemberA 만 변경하는 것을 기대했지만, memberA, memberB 가 둘다 모두 변경되었다. */
            /* -> 값 타입을 서로 다른 Entity 와 공유해서 사용하는 경우 이러한 문제가 발생할 수 있다. */
            /* -> 이러한 현상을 피하기 위해서는 값을 복사해서 사용하도록 코드를 구성해야 한다 */
            assertThat(findMemberA.getHomeAddress().getZipcode()).isEqualTo(findMemberB.getHomeAddress().getZipcode());

            etx.rollback();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

    /*
     * 객체 타입의 한계
     * 1. 항상 값을 복사해서 사용하면 공유참조로 인해 발생하는 부작용을 피할 수 있음
     * 2. 문제는 임베디드 타입처럼 직접 정의한 값 타입은 자바의 기본 타입이 아닌 객체 타입
     * 3. 자바 기본 타입은 값을 대입하면 그 값을 복사하지만 객체 타입은 그렇지 않다
     * 4. 객체 타입은 참조 값을 직접 대입하는 것을 막을 방법이 없음
     * 5. 객체의 공유 참조는 피할 수 없음
     * -> 이 한계를 차단하려면? --> '불변 객체' 를 사용하자
     * */

    /*
     * 불변 객체
     * 1. 객체 타입을 수정할 수 없도록 만들면 이러한 부작용을 원천 차단할 수 있음
     * 2. 값 타입은 '불변 객체 (immutable object)' 로 설계해야 한다
     * 3. 불변 객체 : 생성 시점 이후 절대 값을 변경할 수 없는 객체
     * 4. 생성자로만 값을 설정하고, 수정자 (setter)를 만들지 않으면 됨
     * 5. 참고 -> Integer, String 은 자바가 제공하는 대표적인 불변 객체!
     * -> 즉, 값을 변경하고 싶다면 객체를 새롭게 다시 만들어야 한다
     * -> 불편 타입을 만드는 방법은 여러가지가 있으니 잘 찾아보고 상황에 맞게 적용하도록 하자
     *  */

}
