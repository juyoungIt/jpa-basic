package com.howard.jpabasic.section11.section11_7;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@EntityScan(basePackages = {"com.howard.jpabasic.section11.section11_7"})
public class Section11Test {

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
    @DisplayName("벌크연산 - 벌크 연산은 영속성 컨텍스트를 무시하고 바로 DB 에 쿼리를 수행한다")
    public void bulkProcessingTest() {
        try {
            etx.begin();

            Member memberA = new Member();
            memberA.setName("memberA");
            memberA.setAge(27);
            em.persist(memberA);

            Member memberB = new Member();
            memberB.setName("memberB");
            memberB.setAge(28);
            em.persist(memberB);

            Member memberC = new Member();
            memberC.setName("memberC");
            memberC.setAge(29);
            em.persist(memberC);

            int processed = em.createQuery("update Member m set m.age = :age")
                    .setParameter("age", 30)
                    .executeUpdate();

            Member updatedMemberABeforeClear = em.find(Member.class, memberA.getId());
            Member updatedMemberBBeforeClear = em.find(Member.class, memberB.getId());
            Member updatedMemberCBeforeClear = em.find(Member.class, memberC.getId());

            em.clear(); // 영속성 컨텍스트를 clear 해줘야 벌크 연산으로 인해 발생한 차이를 해소할 수 있다

            Member updatedMemberAAfterClear = em.find(Member.class, memberA.getId());
            Member updatedMemberBAfterClear = em.find(Member.class, memberB.getId());
            Member updatedMemberCAfterClear = em.find(Member.class, memberC.getId());

            assertThat(processed).isEqualTo(3);
            assertThat(updatedMemberABeforeClear.getAge()).isNotEqualTo(updatedMemberAAfterClear.getAge());
            assertThat(updatedMemberBBeforeClear.getAge()).isNotEqualTo(updatedMemberBAfterClear.getAge());
            assertThat(updatedMemberCBeforeClear.getAge()).isNotEqualTo(updatedMemberCAfterClear.getAge());
            assertThat(updatedMemberABeforeClear.getAge()).isEqualTo(27);
            assertThat(updatedMemberBBeforeClear.getAge()).isEqualTo(28);
            assertThat(updatedMemberCBeforeClear.getAge()).isEqualTo(29);
            assertThat(updatedMemberAAfterClear.getAge()).isEqualTo(30);
            assertThat(updatedMemberBAfterClear.getAge()).isEqualTo(30);
            assertThat(updatedMemberCAfterClear.getAge()).isEqualTo(30);

            etx.rollback();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

}
