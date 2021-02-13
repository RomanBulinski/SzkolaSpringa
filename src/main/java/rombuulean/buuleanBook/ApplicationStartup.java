package rombuulean.buuleanBook;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import rombuulean.buuleanBook.catalog.application.CatalogController;
import rombuulean.buuleanBook.catalog.domain.Book;

import java.util.List;

//@RequiredArgsConstructor
@Component
public class ApplicationStartup implements CommandLineRunner {

    private final CatalogController catalogController;
    private final String title;
    private final Long limit;
    private final String name;

    public ApplicationStartup(CatalogController catalogController,
                              @Value("${buuleanBook.catalog.query}") String title,
                              @Value("${buuleanBook.catalog.limit:3}") Long limit,
                              @Value("${buuleanBook.catalog.name:Adam}") String name) {
        this.catalogController = catalogController;
        this.title = title;
        this.limit = limit;
        this.name = name;
    }

    @Override
    public void run(String... args) {
        List<Book> booksByTitle = catalogController.findByTitle(title);
        booksByTitle.stream().limit(limit).forEach(System.out::println);

        List<Book> booksByAuthor = catalogController.findByAuthor(name);
        booksByAuthor.stream().limit(limit).forEach(System.out::println);

    }
}
