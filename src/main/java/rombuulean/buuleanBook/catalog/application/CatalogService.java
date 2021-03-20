package rombuulean.buuleanBook.catalog.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rombuulean.buuleanBook.catalog.application.port.CatalogUseCase;
import rombuulean.buuleanBook.catalog.db.AuthorJpaRepository;
import rombuulean.buuleanBook.catalog.db.BookJpaRepository;
import rombuulean.buuleanBook.catalog.domain.Author;
import rombuulean.buuleanBook.catalog.domain.Book;
import rombuulean.buuleanBook.uploads.application.port.UploadUseCase;
import rombuulean.buuleanBook.uploads.domain.Upload;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static rombuulean.buuleanBook.uploads.application.port.UploadUseCase.SaveUploadCommand;

@Service
@AllArgsConstructor
class CatalogService implements CatalogUseCase {
    /*
       private final CatalogRepository repository;
     */
    private final BookJpaRepository repository;
    private final AuthorJpaRepository authorJpaRepository;
    private final UploadUseCase upload;

    @Override
    public List<Book> findAll() {
        return repository.findAllEager();
    }

    @Override
    public Optional<Book> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Book> findByTitle(String title) {
        return repository.findByTitle(title);
        /*
          earlier version 1
        return repository.findByTitleStartsWithIgnoreCase(title);
          earlier version 2
        return repository.findAll()
                .stream()
                .filter(book -> book.getTitle().toLowerCase().startsWith(title.toLowerCase()))
                .collect(Collectors.toList());
        */
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
        return repository.findByAuthor(name);
//                .findByAuthors_firstNameContainsIgnoreCaseOrAuthors_lastNameContainsIgnoreCase(name,name);
    }

    @Override
    public List<Book> findByTitleAndAuthor(String title, String author) {
        return repository.findAll()
                .stream()
                .filter(book -> book.getTitle().startsWith(title))
//                .filter(book -> book.getAuthor().startsWith(author))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Book addBook(CreateBookCommand createBookCommand) {
        Book book = toBook(createBookCommand);
        return repository.save(book);
    }

    private Book toBook(CreateBookCommand createBookCommand) {
        Book book = new Book(createBookCommand.getTitle(), createBookCommand.getYear(), createBookCommand.getPrice());
        Set<Author> authors = fetchAuthorsById(createBookCommand.getAuthors());
        updateBooks(book, authors);
        return book;
    }

    private void updateBooks(Book book, Set<Author> authors) {
        book.deleteAuthors();
//        book.setAuthors(authors);
        authors.forEach( author -> book.addAuthor(author));
    }

    private Set<Author> fetchAuthorsById(Set<Long> authors) {
        return authors
                .stream()
                .map(authorId -> authorJpaRepository
                        .findById(authorId)
                        .orElseThrow(() -> new IllegalArgumentException("Unable to find author buy id: " + authorId))
                ).collect(Collectors.toSet());
    }


    @Override
    @Transactional
    public UpdateBookResponse updateBook(UpdateBookCommand updateBookCommand) {
        return repository
                .findById(updateBookCommand.getId())
                .map(book -> {
                    updateFields(updateBookCommand, book);
                    /*
                    Jak dodamu adnotacje transactional to to repository save jest niepotrzebne
                    Book updatedBook = updateFields(updateBookCommand, book);
                    repository.save(updatedBook);
                    */
                    return UpdateBookResponse.SUCCESS;
                })
                .orElseGet(() -> new UpdateBookResponse(false, Arrays.asList("Book not found with id: " + updateBookCommand.getId())));
    }

    private Book updateFields(UpdateBookCommand updateBookCommand, Book book) {
        if (updateBookCommand.getTitle() != null) {
            book.setTitle(updateBookCommand.getTitle());
        }
        if (updateBookCommand.getAuthors() != null && !updateBookCommand.getAuthors().isEmpty()) {
            updateBooks(book, fetchAuthorsById(updateBookCommand.getAuthors()));
        }
        if (updateBookCommand.getYear() != null) {
            book.setYear(updateBookCommand.getYear());
        }
        if (updateBookCommand.getPrice() != null) {
            book.setPrice(updateBookCommand.getPrice());
        }
        return book;
    }

    @Override
    public void removeById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void updateBookCover(UpdateBookCoverCommand command) {
        repository.findById(command.getId())
                .ifPresent(book -> {
                    Upload saveUpload = this.upload.save(new SaveUploadCommand(
                            command.getFileName(),
                            command.getContentType(),
                            command.getFile()
                    ));
                    book.setCoverId(saveUpload.getId());
                    repository.save(book);
                });
    }

    @Override
    public void removeBookCover(Long id) {
        repository.findById(id)
                .ifPresent(book -> {
                    if (book.getCoverId() != null) {
                        upload.removeById(book.getCoverId());
                        book.setCoverId(null);
                        repository.save(book);
                    }
                });
    }


}
