package rombuulean.buuleanBook.catalog.application.port;
import rombuulean.buuleanBook.catalog.domain.Author;
import java.util.List;

public interface AuthorsUseCase {

    List<Author> findAll();

}
