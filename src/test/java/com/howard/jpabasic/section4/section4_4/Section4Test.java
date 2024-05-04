package com.howard.jpabasic.section4.section4_4;

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
@EntityScan(basePackages = {"com.howard.jpabasic.section4.section4_4"})
public class Section4Test {

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
    @DisplayName("@Id - 사용자가 직접 Key를 지정해서 insert 하는 경우")
    public void keyGenerationByUserTest() {
        try {
            etx.begin();

            /* 사용자가 직접 Key를 할당하여 저장하는 방식 */
            MemberKeyByUser member = new MemberKeyByUser();
            member.setId("ryanMember");
            member.setUsername("ryan");
            member.setAge(25);
            em.persist(member);

            MemberKeyByUser findMember = em.find(MemberKeyByUser.class, member.getId());
            System.out.println("findMember.id = " + findMember.getId());

            etx.commit();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

    @Test
    @DisplayName("GenerationType.IDENTITY - 기본 키 생성을 데이터베이스에 위임")
    public void keyGenerationByIdentity() {
        try {
            etx.begin();

            /* DB가 자동으로 Key를 생성하도록 처리 */
            MemberKeyByIdentity member = new MemberKeyByIdentity();
            member.setUsername("ryan");
            member.setAge(25);
            /* key를 받아와야하는 문제 때문에 이 시점에 insert query 가 날아간다. */
            /* -> DB에 insert 를 수행해야 key를 알 수 있게 되기 때문 */
            System.out.println("-------------------");
            em.persist(member);
            System.out.println("-------------------");

            MemberKeyByIdentity findMember = em.find(MemberKeyByIdentity.class, member.getId());
            System.out.println("findMember.id = " + findMember.getId());

            etx.commit();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

    @Test
    @DisplayName("GenerationType.SEQUENCE - 기본 키 생성을 데이터베이스에 위임 (공통 Sequence)")
    public void keyGenerationByCommonSequence() {
        try {
            etx.begin();

            /* DB가 자동으로 Key를 생성하도록 처리 */
            MemberKeyByCommonSequence memberA = new MemberKeyByCommonSequence();
            memberA.setUsername("ryan");
            memberA.setAge(25);
            /* key를 받아오기 위해 sequence 를 읽어오는 쿼리가 수행됨 */
            System.out.println("-----------------");
            em.persist(memberA);
            System.out.println("-----------------");

            MemberKeyByCommonSequence memberB = new MemberKeyByCommonSequence();
            memberB.setUsername("choonsik");
            memberB.setAge(20);
            /* key를 받아오기 위해 sequence 를 읽어오는 쿼리가 수행됨 */
            System.out.println("-----------------");
            em.persist(memberB);
            System.out.println("-----------------");

            MemberKeyByCommonSequence findMemberA = em.find(MemberKeyByCommonSequence.class, memberA.getId());
            System.out.println("findMemberA.id = " + findMemberA.getId());
            MemberKeyByCommonSequence findMemberB = em.find(MemberKeyByCommonSequence.class, memberB.getId());
            System.out.println("findMemberB.id = " + findMemberB.getId());

            etx.commit();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

    @Test
    @DisplayName("GenerationType.SEQUENCE - 기본 키 생성을 데이터베이스에 위임 (테이블 별로 분리된 Sequence)")
    public void keyGenerationBySeparatedSequence() {
        try {
            etx.begin();

            /* DB가 자동으로 Key를 생성하도록 처리 */
            MemberKeyBySeparatedSequence memberA = new MemberKeyBySeparatedSequence();
            memberA.setUsername("ryan");
            memberA.setAge(25);
            /* key를 받아오기 위해 sequence 를 읽어오는 쿼리가 수행됨 */
            System.out.println("-----------------");
            em.persist(memberA);
            System.out.println("-----------------");

            MemberKeyBySeparatedSequence memberB = new MemberKeyBySeparatedSequence();
            memberB.setUsername("choonsik");
            memberB.setAge(20);
            /* key를 받아오기 위해 sequence 를 읽어오는 쿼리가 수행됨 */
            System.out.println("-----------------");
            em.persist(memberB);
            System.out.println("-----------------");

            MemberKeyBySeparatedSequence findMemberA = em.find(MemberKeyBySeparatedSequence.class, memberA.getId());
            System.out.println("findMemberA.id = " + findMemberA.getId());
            MemberKeyBySeparatedSequence findMemberB = em.find(MemberKeyBySeparatedSequence.class, memberB.getId());
            System.out.println("findMemberB.id = " + findMemberB.getId());

            etx.commit();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

    @Test
    @DisplayName("GenerationType.TABLE - 기본 키 생성을 데이터베이스에 위임 (별도의 Table을 생성)")
    public void keyGenerationByTable() {
        try {
            etx.begin();

            /* DB가 자동으로 Key를 생성하도록 처리 */
            MemberKeyByTable memberA = new MemberKeyByTable();
            memberA.setUsername("ryan");
            memberA.setAge(25);
            em.persist(memberA);

            MemberKeyByTable memberB = new MemberKeyByTable();
            memberB.setUsername("choonsik");
            memberB.setAge(20);
            em.persist(memberB);

            MemberKeyByTable findMemberA = em.find(MemberKeyByTable.class, memberA.getId());
            System.out.println("findMemberA.id = " + findMemberA.getId());
            MemberKeyByTable findMemberB = em.find(MemberKeyByTable.class, memberB.getId());
            System.out.println("findMemberB.id = " + findMemberB.getId());

            etx.commit();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

}
