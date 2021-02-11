package rombuulean.buuleanBook;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class BuuleanBookWebBookstoreApplication implements CommandLineRunner {

    private final CatalogService catalogService;

    public BuuleanBookWebBookstoreApplication(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    public static void main(String[] args) {
        SpringApplication.run(BuuleanBookWebBookstoreApplication.class, args);
    }

    @Override
    public void run(String... args) {
//		CatalogService service = new CatalogService();
        List<Book> books = catalogService.findByTitle("Pan Tadeusz");
        books.forEach(System.out::println);
    }

}
