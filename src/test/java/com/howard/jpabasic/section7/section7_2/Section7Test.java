package com.howard.jpabasic.section7.section7_2;

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

@SpringBootTest
@EntityScan(basePackages = {"com.howard.jpabasic.section7.section7_2"})
public class Section7Test {

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
    @DisplayName("@MappedSuperclass - 각 Entity들이 공통으로 가지는 속성값을 한 곳에서 관리한다.")
    public void mappedSuperclassTest() {
        try {
            etx.begin();

            /* Team Entity 생성 및 영속화 */
            Team team = new Team();
            team.setName("teamA");
            team.setCreateAt(LocalDateTime.now());
            team.setLastModifiedAt(LocalDateTime.now());
            em.persist(team);

            /* Member Entity 생성 및 영속화 */
            Member member = new Member();
            member.setName("ryan");
            member.setAge(25);
            member.setTeamRelation(team);
            member.setCreateAt(LocalDateTime.now());
            member.setLastModifiedAt(LocalDateTime.now());
            em.persist(member);

            /* 영속화한 Member Entity 조회 */
            Member findMember = em.find(Member.class, member.getId());
            System.out.println("findMember.id = " + findMember.getId());
            System.out.println("findMember.name = " + findMember.getName());
            System.out.println("findMember.age = " + findMember.getAge());
            System.out.println("findMember.createAt = " + findMember.getCreateAt());
            System.out.println("findMember.lastModifiedAt = " + findMember.getLastModifiedAt());
            System.out.println("findMember.team.id = " + findMember.getTeam().getId());
            System.out.println("findMember.team.name = " + findMember.getTeam().getName());
            System.out.println("findMember.team.createAt = " + findMember.getTeam().getCreateAt());
            System.out.println("findMember.team.lastModifiedAt = " + findMember.getTeam().getLastModifiedAt());

            etx.commit();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

}
