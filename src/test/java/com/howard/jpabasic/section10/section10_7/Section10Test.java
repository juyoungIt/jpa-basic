package com.howard.jpabasic.section10.section10_7;

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
@EntityScan(basePackages = {"com.howard.jpabasic.section10.section10_7"})
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
    @DisplayName("JPQL 타입표현 - Enum 사용 시에는 패키지명을 함께 넣어줘야 한다.")
    public void enumTypeTest() {
        try {
            etx.begin();

            Member memberA = new Member();
            memberA.setUsername("memberA");
            memberA.setMemberType(MemberType.ADMIN);
            memberA.setAge(27);
            em.persist(memberA);

            Member memberB = new Member();
            memberB.setUsername("memberB");
            memberB.setMemberType(MemberType.USER);
            memberB.setAge(23);
            em.persist(memberB);

            List<Member> findMembers = em
                    .createQuery(
                            "select m from com.howard.jpabasic.section10.section10_7.Member m " +
                                    "where m.memberType = com.howard.jpabasic.section10.section10_7.MemberType.ADMIN",
                            Member.class)
                    .getResultList();

            assertThat(findMembers).hasSize(1);
            assertThat(findMembers)
                    .extracting(Member::getUsername)
                    .containsExactly("memberA");
            assertThat(findMembers)
                    .extracting(Member::getMemberType)
                    .containsExactly(MemberType.ADMIN);
            assertThat(findMembers)
                    .extracting(Member::getAge)
                    .containsExactly(27);

            etx.rollback();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

    @Test
    @DisplayName("JPQL 타입표현 - 엔티티 타입")
    public void entityTypeTest() {
        try {
            etx.begin();

            Album album = new Album();
            album.setName("오랜만이야");
            album.setPrice(30_000);
            album.setArtist("최유리");
            em.persist(album);

            Book book = new Book();
            book.setName("나를 아끼는 마음");
            book.setPrice(15_000);
            book.setAuthor("김져니");
            book.setIsbn("979-11-974867-2-2");
            em.persist(book);

            Movie movie = new Movie();
            movie.setName("탑건2 매버릭");
            movie.setPrice(20_000);
            movie.setDirector("조셉 코신스키");
            movie.setActor("톰 크루즈");
            em.persist(movie);

            List<Product> results = em.createQuery(
                    "select p from com.howard.jpabasic.section10.section10_7.Product p " +
                            "where type(p) = com.howard.jpabasic.section10.section10_7.Album",
                            Product.class)
                    .getResultList();

            assertThat(results).hasSize(1);
            assertThat(results)
                    .extracting(Product::getName)
                    .containsExactly("오랜만이야");
            assertThat(results)
                    .extracting(Product::getPrice)
                    .containsExactly(30_000);

            etx.rollback();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

}
