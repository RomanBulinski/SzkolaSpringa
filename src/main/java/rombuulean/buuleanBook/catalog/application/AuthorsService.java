package rombuulean.buuleanBook.catalog.application;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;
import rombuulean.buuleanBook.catalog.application.port.AuthorsUseCase;
import rombuulean.buuleanBook.catalog.application.port.CatalogUseCase;
import rombuulean.buuleanBook.catalog.db.AuthorJpaRepository;
import rombuulean.buuleanBook.catalog.db.BookJpaRepository;
import rombuulean.buuleanBook.catalog.domain.Author;
import rombuulean.buuleanBook.catalog.domain.Book;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AuthorsService implements AuthorsUseCase {
    private final AuthorJpaRepository authorJpaRepository;
    private final BookJpaRepository bookJpaRepository;

    @Override
    public List<Author> findAll() {
        return authorJpaRepository.findAll();
    }

    @Override
    public List<Author> findByName(String name) {
        return authorJpaRepository.findAuthorsByName( name );
    }

    @Override
    public Optional<Author> findById(Long id) {
        return authorJpaRepository.findById(id);
    }

    @Override
    public List<Author> findByBookTitle(String bookTitle) {
        return authorJpaRepository.findAuthorsByBooksTitle( bookTitle);
    }

    @Override
    public Author addAuthor(CreateAuthorCommand createAuthorCommand) {
        Author author = new Author(createAuthorCommand.getName());
        return authorJpaRepository.save(author);
    }

    @Override
    public UpdateAuthorResponse updateAuthor(UpdateAuthorCommand updateAuthorCommand) {
        return authorJpaRepository
                .findById(updateAuthorCommand.getId())
                .map(author -> {
                    Author updatedAuthor = updateFields(updateAuthorCommand, author);
                    authorJpaRepository.save(updatedAuthor);
                    return UpdateAuthorResponse.SUCCESS;
                })
                .orElseGet(() -> new UpdateAuthorResponse(false, Arrays.asList("Book not found with id: " + updateAuthorCommand.getId())));
    }

    private Author updateFields( UpdateAuthorCommand updateAuthorCommand, Author author) {
        if (updateAuthorCommand.getName() != null) {
            author.setName(updateAuthorCommand.getName());
        }
        if (updateAuthorCommand.getBooks() != null && !updateAuthorCommand.getBooks().isEmpty()) {
            author.setBooks(fetchBooksById( updateAuthorCommand.getBooks() ));
        }
        return author;
    }

    private Set<Book> fetchBooksById(Set<Book> books) {
        return books
                .stream()
                .map(book -> bookJpaRepository
                        .findById(book.getId())
                        .orElseThrow(() -> new IllegalArgumentException("Unable to find book buy id: " + book.getId()))
                ).collect(Collectors.toSet());
    }


}
