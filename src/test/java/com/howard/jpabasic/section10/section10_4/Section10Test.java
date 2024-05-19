package com.howard.jpabasic.section10.section10_4;

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
@EntityScan(basePackages = {"com.howard.jpabasic.section10.section10_4"})
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
    @DisplayName("페이징 - JPA는 .setFirstResult(), setMaxResults() 로 페이징 쿼리를 추상화")
    public void pagingTest() {
        try {
            etx.begin();

            /* 테스트를 위해 100 개의 테스트용 데이터를 세팅 */
            for (int i=100 ; i>=0 ; i--) {
                Member member = new Member();
                member.setUsername("member" + i);
                member.setAge(i);
                em.persist(member);
            }

            List<Member> findMembers = em
                    .createQuery(
                            "select m " +
                                    "from com.howard.jpabasic.section10.section10_4.Member m " +
                                    "order by m.age asc",
                            Member.class)
                    .setFirstResult(0)
                    .setMaxResults(5)
                    .getResultList();

            assertThat(findMembers.size()).isEqualTo(5);
            assertThat(findMembers)
                    .extracting(Member::getUsername)
                    .isEqualTo(List.of("member0", "member1", "member2", "member3", "member4"));
            assertThat(findMembers)
                    .extracting(Member::getAge)
                    .isEqualTo(List.of(0, 1, 2, 3, 4));

            findMembers = em
                    .createQuery(
                            "select m " +
                                    "from com.howard.jpabasic.section10.section10_4.Member m " +
                                    "order by m.age asc",
                            Member.class)
                    .setFirstResult(10)
                    .setMaxResults(5)
                    .getResultList();

            assertThat(findMembers.size()).isEqualTo(5);
            assertThat(findMembers)
                    .extracting(Member::getUsername)
                    .isEqualTo(List.of("member10", "member11", "member12", "member13", "member14"));
            assertThat(findMembers)
                    .extracting(Member::getAge)
                    .isEqualTo(List.of(10, 11, 12, 13, 14));

            etx.rollback();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

}
