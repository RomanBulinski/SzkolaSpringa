package rombuulean.buuleanBook.catalog.db;

import org.springframework.data.jpa.repository.JpaRepository;
import rombuulean.buuleanBook.catalog.domain.Author;
import java.util.List;

public interface AuthorJpaRepository extends JpaRepository<Author, Long> {

    List<Author> findAuthorsByFirstNameAndLastName(String firstName, String lastName);
    List<Author> findByFirstName(String firstName);
    List<Author> findByLastName(String firstName);



}
