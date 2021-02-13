package rombuulean.buuleanBook;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import rombuulean.buuleanBook.catalog.application.CatalogController;
import rombuulean.buuleanBook.catalog.domain.Book;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ApplicationStartup implements CommandLineRunner {

    private final CatalogController catalogController;

    @Override
    public void run(String... args) {
        List<Book> books = catalogController.findByTitle("Pan");
        books.forEach(System.out::println);
    }
}
