package com.howard.jpabasic.section11.section11_4;

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
@EntityScan(basePackages = {"com.howard.jpabasic.section11.section11_4"})
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
    @DisplayName("type() -> 조회 대상을 타입으로 한정할 수 있음")
    public void typeTest() {
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

            /* Item 중에 Book 과 Movie 를 조회하려고 하는 경우 */
            List<Item> results = em
                    .createQuery("select i from Item i where type(i) in (Book, Movie)", Item.class)
                    .getResultList();

            assertThat(results).hasSize(2);
            assertThat(results)
                    .extracting(Item::getName)
                    .containsExactly("나를 아끼는 마음", "탑건2 매버릭");

            etx.rollback();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

    @Test
    @DisplayName("treat() - 상속 구조에서 부모타입을 특정 자식 타입으로 다루는 경우 사용한다.")
    public void treatTest() {
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

            /* Item 중 artist 가 '최유리' 인 것을 찾는다 */
            List<Item> results = em
                    .createQuery("select i from Item i where treat(i as Album).artist = '최유리'", Item.class)
                    .getResultList();

            assertThat(results).hasSize(1);
            assertThat(results).extracting(Item::getName).containsExactly("오랜만이야");
            assertThat(results).extracting(Item::getPrice).containsExactly(30_000);
            assertThat(((Album) results.get(0)).getArtist()).isEqualTo("최유리");

            etx.rollback();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

}
