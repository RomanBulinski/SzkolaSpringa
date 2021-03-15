package rombuulean.buuleanBook.catalog.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rombuulean.buuleanBook.catalog.domain.Author;
import java.util.List;

public interface AuthorJpaRepository extends JpaRepository<Author, Long> {

    List<Author> findAuthorsByFirstNameAndLastName(String firstName, String lastName);
    List<Author> findByFirstName(String firstName);
    List<Author> findByLastName(String firstName);

    @Query(" SELECT b FROM Author b JOIN b.books a" +
            " WHERE " +
            " lower(a.title) LIKE  lower(concat('%',:title ,'%'))"
    )
    List<Author> findAuthorsByBooksTitle(@Param("title")String title);

}
