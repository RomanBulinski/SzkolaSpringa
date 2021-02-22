package rombuulean.buuleanBook.catalog.application;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import rombuulean.buuleanBook.catalog.application.port.CatalogUseCase;
import rombuulean.buuleanBook.catalog.domain.Book;
import rombuulean.buuleanBook.catalog.domain.CatalogRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
class CatalogService implements CatalogUseCase {

    private final CatalogRepository repository;

    @Override
    public List<Book> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Book> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Book> findByTitle(String title) {
        return repository.findAll()
                .stream()
                .filter(book -> book.getTitle().toLowerCase().startsWith(title.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Book> findOneByTitle(String title) {
        return repository.findAll()
                .stream()
                .filter(book -> book.getTitle().startsWith(title))
                .findFirst();
    }

    @Override
    public List<Book> findByAuthor(String name) {
        return repository.findAll()
                .stream()
                .filter(book -> book.getAuthor().contains(name))
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> findByTitleAndAuthor(String title, String author) {
        return repository.findAll()
                .stream()
                .filter(book -> book.getTitle().startsWith(title))
                .filter(book -> book.getAuthor().startsWith(author))
                .collect(Collectors.toList());
    }

    @Override
    public void addBook(CreateBookCommand command) {
        Book book = command.toBook();
        repository.save(book);
    }

    @Override
    public UpdateBookResponse updateBook(UpdateBookCommand command) {
        return repository
                .findById(command.getId())
                .map(book -> {
                    Book updatedBook = command.updateFields(book);
                    repository.save(updatedBook);
                    return UpdateBookResponse.SUCCESS;
                })
                .orElseGet(() -> new UpdateBookResponse(false, Arrays.asList("Book not found with id: " + command.getId())));
    }

    @Override
    public void removeById(Long id) {
        repository.removeById(id);
    }

}
