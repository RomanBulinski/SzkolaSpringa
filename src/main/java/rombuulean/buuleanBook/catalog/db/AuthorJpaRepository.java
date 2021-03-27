package rombuulean.buuleanBook.catalog.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rombuulean.buuleanBook.catalog.domain.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorJpaRepository extends JpaRepository<Author, Long> {

    List<Author> findAuthorsByName(String name);

    @Query(" SELECT b FROM Author b JOIN b.books a" +
            " WHERE " +
            " lower(a.title) LIKE  lower(concat('%',:title ,'%'))"
    )
    List<Author> findAuthorsByBooksTitle(@Param("title") String title);

    Optional<Author> findByNameIgnoreCase(String name);

}
