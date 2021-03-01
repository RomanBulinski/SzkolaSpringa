package rombuulean.buuleanBook.catalog.db;

import org.springframework.data.jpa.repository.JpaRepository;
import rombuulean.buuleanBook.catalog.domain.Book;

public interface BookJpaRepository extends JpaRepository<Book, Long> {
}
