package rombuulean.buuleanBook.catalog.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rombuulean.buuleanBook.catalog.domain.Book;

import java.util.List;

public interface BookJpaRepository extends JpaRepository<Book, Long> {

    @Query(" SELECT DISTINCT b FROM Book b " +
            " JOIN FETCH " +
            " b.authors")
    List<Book> findAllEager();

    //another version of method
    List<Book> findByAuthors_nameContainsIgnoreCase(String name);

    @Query(" SELECT b FROM Book b JOIN b.authors a" +
            " WHERE " +
            " lower(a.name) LIKE  lower(concat('%',:name ,'%'))")
    List<Book> findByAuthor(@Param("name")String name);

    List<Book> findByTitleStartsWithIgnoreCase(String title);

    @Query(" SELECT b FROM Book b " +
            " WHERE " +
            " lower(b.title) LIKE  lower(concat('%',:title ,'%'))")
    List<Book> findByTitle(@Param("title")String title);


    @Query(" SELECT b " +
            " FROM Book b " +
            " JOIN b.authors a " +
            " WHERE " +
            "lower(b.title) LIKE lower (concat('%', : title, '%'))" +
            " AND " +
            " lower(a.name) LIKE lower (concat('%', : author, '%'))"
    )
    List<Book> findByTitleAndAuthor(@Param("title")String title,
                                    @Param("author")String author);


}
