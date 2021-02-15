package rombuulean.buuleanBook;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import rombuulean.buuleanBook.catalog.application.port.CatalogUseCase;
import rombuulean.buuleanBook.catalog.application.port.CatalogUseCase.CreateBookCommand;
import rombuulean.buuleanBook.catalog.domain.Book;

import java.util.List;
import java.util.Optional;

import static rombuulean.buuleanBook.catalog.application.port.CatalogUseCase.*;

//@RequiredArgsConstructor
@Component
public class ApplicationStartup implements CommandLineRunner {

    private final CatalogUseCase catalog;
    private final String title;
    private final Long limit;
    private final String name;

    public ApplicationStartup(CatalogUseCase catalog,
                              @Value("${buuleanBook.catalog.query}") String title,
                              @Value("${buuleanBook.catalog.limit:3}") Long limit,
                              @Value("${buuleanBook.catalog.name:Adam}") String name) {
        this.catalog = catalog;
        this.title = title;
        this.limit = limit;
        this.name = name;
    }

    @Override
    public void run(String... args) {
        initDate();
        findByTitle();
        findAndUpdate();
        findByTitle();
    }

    private void initDate() {
        catalog.addBook(new CreateBookCommand("Via Carpatia", "Ziemowit Szczerek", 1834));
        catalog.addBook(new CreateBookCommand("Boże igrzysko", "Norman Davis", 1884));
        catalog.addBook(new CreateBookCommand("Monte Cassino", "Melchior Wańkowicz", 1960));
        catalog.addBook(new CreateBookCommand("Na tropach smętka", "Melchior Wańkowicz", 1936));
        catalog.addBook(new CreateBookCommand("Jadąc do Badadag", "Andrzej Stasiuk", 2000));
        catalog.addBook(new CreateBookCommand("Wiersze", "Juliusz Słowacki", 1850));
        catalog.addBook(new CreateBookCommand("Wiersze", "Wisława Szymborska", 2000));
    }

    private void findByTitle() {
        List<Book> booksByTitle = catalog.findByTitle(title);
        booksByTitle.stream().limit(limit).forEach(System.out::println);
    }

    private void findAndUpdate() {
        System.out.println("Updating book : ");
        catalog.findOneByTitleAndAuthor("Wiersze", "Wisława")
                .ifPresent(book -> {
                    UpdateBookCommand command = new UpdateBookCommand(
                            book.getId(),
                            "Wiersze wybrane",
                            book.getAuthor(),
                            book.getYear()
                            );
                    catalog.updateBook(command);
                });
    }

}
