package rombuulean.buuleanBook.catalog.db;

import org.springframework.data.jpa.repository.JpaRepository;
import rombuulean.buuleanBook.catalog.domain.Author;

public interface AuthorJpaRepository extends JpaRepository<Author, Long> {
}
