package rombuulean.buuleanBook.catalog.application;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;
import rombuulean.buuleanBook.catalog.application.port.AuthorsUseCase;
import rombuulean.buuleanBook.catalog.db.AuthorJpaRepository;
import rombuulean.buuleanBook.catalog.domain.Author;
import rombuulean.buuleanBook.catalog.domain.Book;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class AuthorsService implements AuthorsUseCase {
    private final AuthorJpaRepository authorJpaRepository;

    @Override
    public List<Author> findAll() {
        return authorJpaRepository.findAll();
    }

    @Override
    public List<Author> findByFirstName(String firstName) {
        return authorJpaRepository.findByFirstName(firstName);
    }

    @Override
    public List<Author> findByLastName(String lastName) {
        return authorJpaRepository.findByLastName(lastName);
    }

    @Override
    public List<Author> findByFirstAndLastName(String firstName, String lastName) {
        return authorJpaRepository.findAuthorsByFirstNameAndLastName(firstName,lastName );
    }

    @Override
    public Optional<Author> findById(Long id) {
        return authorJpaRepository.findById(id);
    }

//    @Override
//    public void removeById(Long id) {
//        authorJpaRepository.deleteById(id);
//    }

    @Override
    public Author addAuthor(CreateAuthorCommand createAuthorCommand) {
        Author author = new Author(createAuthorCommand.getFirstName(),  createAuthorCommand.getLastName());
        return authorJpaRepository.save(author);
    }



}
