package rombuulean.buuleanBook.catalog.application.port;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import rombuulean.buuleanBook.catalog.application.AuthorsService;
import rombuulean.buuleanBook.catalog.domain.Author;
import rombuulean.buuleanBook.catalog.domain.Book;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.Collections.emptyList;

public interface AuthorsUseCase {

    List<Author> findAll();

    List<Author> findByFirstName(String firstName);

    List<Author> findByLastName(String lastName);

    List<Author> findByFirstAndLastName(String firstName, String lastName);

    Optional<Author> findById(Long id);

    List<Author> findByBookTitle(String bookTitle);

    //    void removeById(Long id);

    Author addAuthor(CreateAuthorCommand createAuthorCommand);

    @Value
    class CreateAuthorCommand {
        String firstName;
        String lastName;
    }

    UpdateAuthorResponse updateAuthor(UpdateAuthorCommand updateAuthorCommand);

    @Value
    @Builder
    @AllArgsConstructor
    class UpdateAuthorCommand {
        Long id;
        String firstName;
        String lastName;
        Set<Book> books;
    }

    @Value
    class UpdateAuthorResponse {
        public static UpdateAuthorResponse SUCCESS = new UpdateAuthorResponse(true, emptyList());
        boolean success;
        List<String> errors;
    }
}
