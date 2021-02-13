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

    public ApplicationStartup(CatalogController catalogController,
                              @Value("${buuleanBook.catalog.query}") String title,
                              @Value("${buuleanBook.catalog.limit:3}") Long limit) {
        this.catalogController = catalogController;
        this.title = title;
        this.limit = limit;
    }

    @Override
    public void run(String... args) {
        List<Book> books = catalogController.findByTitle(title);
        books.stream().limit(limit).forEach(System.out::println);
    }
}
