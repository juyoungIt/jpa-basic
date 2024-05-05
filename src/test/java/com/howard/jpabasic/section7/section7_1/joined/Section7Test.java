package com.howard.jpabasic.section7.section7_1.joined;

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
@EntityScan(basePackages = {"com.howard.jpabasic.section7.section7_1.joined"})
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
    @DisplayName("InheritanceType.JOINED - JOINED 전략으로 상속관계를 표현하는 테이블을 생성한다.")
    public void inheritanceTypeJoinedTest() {
        try {
            etx.begin();

            /* Album Entity 생성 및 영속화 */
            Album album = new Album();
            album.setName("오랜만이야");
            album.setPrice(30_000);
            album.setArtist("최유리");
            em.persist(album);

            /* Book Entity 생성 및 영속화 */
            Book book = new Book();
            book.setName("나를 아끼는 마음");
            book.setPrice(15_000);
            book.setAuthor("김져니");
            book.setIsbn("979-11-974867-2-2");
            em.persist(book);

            /* Movie Entity 생성 및 영속화 */
            Movie movie = new Movie();
            movie.setName("탑건2 매버릭");
            movie.setPrice(20_000);
            movie.setDirector("조셉 코신스키");
            movie.setActor("톰 크루즈");
            em.persist(movie);

            /* select query 를 확인하기 위한 목적으로 추가한 코드 */
            em.flush();
            em.clear();

            /* 저장한 Entity 조회 */
            Album findAlbum = em.find(Album.class, album.getId());
            System.out.println("findAlbum.id = " + findAlbum.getId());
            System.out.println("findAlbum.name = " + findAlbum.getName());
            System.out.println("findAlbum.price = " + findAlbum.getPrice());
            System.out.println("findAlbum.artist = " + findAlbum.getArtist());

            Book findBook = em.find(Book.class, book.getId());
            System.out.println("findBook.id = " + findBook.getId());
            System.out.println("findBook.name = " + findBook.getName());
            System.out.println("findBook.price = " + findBook.getPrice());
            System.out.println("findBook.author = " + findBook.getAuthor());
            System.out.println("findBook.isbn = " + findBook.getIsbn());

            Movie findMovie = em.find(Movie.class, movie.getId());
            System.out.println("findMovie.id = " + findMovie.getId());
            System.out.println("findMovie.name = " + findMovie.getName());
            System.out.println("findMovie.price = " + findMovie.getPrice());
            System.out.println("findMovie.director = " + findMovie.getDirector());
            System.out.println("findMovie.actor = " + findMovie.getActor());

            etx.commit();
        } catch (Exception e) {
            etx.rollback();
        } finally {
            em.close();
        }
    }

}
