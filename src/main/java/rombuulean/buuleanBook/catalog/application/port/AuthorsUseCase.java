package rombuulean.buuleanBook.catalog.application.port;

import lombok.Value;
import rombuulean.buuleanBook.catalog.application.AuthorsService;
import rombuulean.buuleanBook.catalog.domain.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorsUseCase {

    List<Author> findAll();

    List<Author> findByFirstName(String firstName);

    List<Author> findByLastName(String lastName);

    List<Author> findByFirstAndLastName(String firstName, String lastName);

    Optional<Author> findById(Long id);

    //    void removeById(Long id);
    Author addAuthor(AuthorsService.CreateAuthorCommand createAuthorCommand);

    @Value
    class CreateAuthorCommand {
        String firstName;
        String lastName;
    }

}
