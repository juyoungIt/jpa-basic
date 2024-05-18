package com.howard.jpabasic.section10.section10_3;

import jakarta.persistence.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@EntityScan(basePackages = {"com.howard.jpabasic.section10.section10_3"})
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
    @DisplayName("엔티티 프로젝션 - 엔티티 프로젝션의 결과는 모두 영속성 컨텍스트에서 관리된다.")
    public void entityProjectionTest() {
        try {
            etx.begin();

            Member memberA = new Member();
            memberA.setUsername("ryan");
            memberA.setAge(27);
            em.persist(memberA);

            Member memberB = new Member();
            memberB.setUsername("choonsik");
            memberB.setAge(25);
            em.persist(memberB);

            Member memberC = new Member();
            memberC.setUsername("apeach");
            memberC.setAge(19);
            em.persist(memberC);

            /* JPQL 쿼리를 실행하기 전 영속성 컨텍스트를 비운다. */
            em.flush();
            em.clear();

            /* 여기서 프로젝션된 Entity 들은 모두 영속성 컨텍스트의 관리 대상이 된다. */
            List<Member> findMembers = em
                    .createQuery("select m from Member m", Member.class)
                    .getResultList();

            for (Member member : findMembers) {
                member.setAge(30);
            }
            /* 그래서 flush 시점에 업데이트 쿼리가 날아간다. */
            em.flush();

            assertThat(findMembers)
                    .extracting(Member::getAge)
                    .isEqualTo(List.of(30, 30, 30));

            etx.rollback();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

    @Test
    @DisplayName("엔티티 프로젝션 - 연관관계를 가진 필드를 조회한 경우 -> 조인이 들어간다는 사실에 주의하자")
    public void entityProjectionTest2() {
        try {
            etx.begin();

            /* Team Entity 영속화 */
            Team team = new Team();
            team.setName("friends");
            em.persist(team);

            /* Member Entity 영속화 */
            Member member = new Member();
            member.setUsername("ryan");
            member.setAge(27);
            /* Team & Member 사이의 연관관계 설정 */
            member.setTeam(team);
            team.getMembers().add(member);
            em.persist(member);

            /* 주의! - 이런 식으로 구성하면 명시적이지 않게 조인 쿼리가 날아가서 별로 권장하지 않는다. */
            Team findTeamA = em
                    .createQuery(
                            "select m.team from com.howard.jpabasic.section10.section10_3.Member m",
                            Team.class
                    )
                    .getSingleResult();

            assertThat(findTeamA.getName()).isEqualTo("friends");
            assertThat(findTeamA.getMembers())
                    .extracting(Member::getUsername)
                    .isEqualTo(List.of("ryan"));
            assertThat(findTeamA.getMembers())
                    .extracting(Member::getAge)
                    .isEqualTo(List.of(27));

            /* 그래서 다음과 같이 명시적으로 조인이 들어나도록 작성하는 것을 권장한다 */
            Team findTeamB = em
                    .createQuery(
                            "select m.team from com.howard.jpabasic.section10.section10_3.Member m join m.team t",
                            Team.class
                    )
                    .getSingleResult();

            assertThat(findTeamB.getName()).isEqualTo("friends");
            assertThat(findTeamB.getMembers())
                    .extracting(Member::getUsername)
                    .isEqualTo(List.of("ryan"));
            assertThat(findTeamB.getMembers())
                    .extracting(Member::getAge)
                    .isEqualTo(List.of(27));

            etx.rollback();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

    @Test
    @DisplayName("임베디드 타입 프로젝션 - 임베디드 타입도 특별한 처리없이 간편하게 프로젝션 대상이 될 수 있다.")
    public void embeddedTypeProjectionTest() {
        try {
            etx.begin();

            Order order = new Order();
            order.setAddress(new Address("testCity", "testStreet", "testZipcode"));
            em.persist(order);

            Address findAddress = em
                    .createQuery(
                            "select o.address from com.howard.jpabasic.section10.section10_3.Order o",
                            Address.class
                    )
                    .getSingleResult();

            assertThat(findAddress.getCity()).isEqualTo("testCity");
            assertThat(findAddress.getStreet()).isEqualTo("testStreet");
            assertThat(findAddress.getZipcode()).isEqualTo("testZipcode");

            etx.rollback();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

    @Test
    @DisplayName("스칼라 타입 프로젝션 - Query 타입으로 그 결과를 받아오는 경우")
    public void scalarTypeProjectWithQueryTypeTest() {
        try {
            etx.begin();

            Member member = new Member();
            member.setUsername("ryan");
            member.setAge(27);
            em.persist(member);

            /* 다음과 같이 타입을 추론할 수 없으므로 Object Type 으로 그 결과를 받게 된다. */
            Object object = em
                    .createQuery("select m.username, m.age from com.howard.jpabasic.section10.section10_3.Member m")
                    .getSingleResult();

            /* Object -> Object Array 로 변환 */
            Object[] result = (Object[]) object;

            assertThat(result[0]).isEqualTo("ryan");
            assertThat(result[1]).isEqualTo(27);

            etx.rollback();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

    @Test
    @DisplayName("스칼라 타입 프로젝션 - Object[] 타입으로 받으면 Query Type 에서의 Casting 과정을 생략가능")
    public void scalarTypeProjectWithObjectArrayTypeTest() {
        try {
            etx.begin();

            Member member = new Member();
            member.setUsername("ryan");
            member.setAge(27);
            em.persist(member);

            Object[] result = em
                    .createQuery(
                            "select m.username, m.age from com.howard.jpabasic.section10.section10_3.Member m",
                            Object[].class)
                    .getSingleResult();

            assertThat(result[0]).isEqualTo("ryan");
            assertThat(result[1]).isEqualTo(27);

            etx.rollback();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

    @Test
    @DisplayName("스칼라 타입 프로젝션 - new 명령어를 사용해 DTO 로 받아오는 경우")
    public void scalarTypeProjectWithDtoTest() {
        try {
            etx.begin();

            Member member = new Member();
            member.setUsername("ryan");
            member.setAge(27);
            em.persist(member);

            /* 다음과 같이 DTO 를 사용해서 그 결과를 projection 할 수 있다 */
            MemberDTO result = em
                    .createQuery(
                            "select new com.howard.jpabasic.section10.section10_3.MemberDTO(m.username, m.age) " +
                            "from com.howard.jpabasic.section10.section10_3.Member m",
                            MemberDTO.class
                    )
                    .getSingleResult();

            assertThat(result.getUsername()).isEqualTo("ryan");
            assertThat(result.getAge()).isEqualTo(27);

            etx.rollback();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

}
