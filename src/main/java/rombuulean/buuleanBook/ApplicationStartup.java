package rombuulean.buuleanBook;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import rombuulean.buuleanBook.catalog.application.port.CatalogUseCase;
import rombuulean.buuleanBook.catalog.application.port.CatalogUseCase.CreateBookCommand;
import rombuulean.buuleanBook.catalog.domain.Book;
import rombuulean.buuleanBook.order.application.port.PlaceOrderUseCase;
import rombuulean.buuleanBook.order.application.port.PlaceOrderUseCase.PlaceOrderCommand;
import rombuulean.buuleanBook.order.application.port.PlaceOrderUseCase.PlaceOrderResponse;
import rombuulean.buuleanBook.order.application.port.QueryOrderUseCase;
import rombuulean.buuleanBook.order.domain.OrderItem;
import rombuulean.buuleanBook.order.domain.Recipient;

import java.math.BigDecimal;
import java.util.List;

import static rombuulean.buuleanBook.catalog.application.port.CatalogUseCase.*;

//@RequiredArgsConstructor
@Component
public class ApplicationStartup implements CommandLineRunner {

    private final CatalogUseCase catalog;
    private final String title;
    private final Long limit;
    private final String name;
    private final PlaceOrderUseCase placeOrder;
    private final QueryOrderUseCase queryOrder;

    public ApplicationStartup(CatalogUseCase catalog,
                              PlaceOrderUseCase placeOrder,
                              QueryOrderUseCase queryOrder,
                              @Value("${buuleanBook.catalog.query}") String title,
                              @Value("${buuleanBook.catalog.limit:3}") Long limit,
                              @Value("${buuleanBook.catalog.name:Adam}") String name) {
        this.catalog = catalog;
        this.placeOrder = placeOrder;
        this.queryOrder = queryOrder;
        this.title = title;
        this.limit = limit;
        this.name = name;
    }

    @Override
    public void run(String... args) {
        initDate();
        serachCatalog();
        placeOrder();
    }

    private void placeOrder() {
        Book montecassino = catalog.findOneByTitle("Monte").orElseThrow(()-> new IllegalStateException("Cannot find a book"));
        Book Wiersze = catalog.findOneByTitle("Wiersze").orElseThrow(()-> new IllegalStateException("Cannot find a book"));

        Recipient recipient = Recipient.builder()
                .name("Tytus Kowalski")
                .phone(("333-333-333-33"))
                .street("Warszawska")
                .city("Warszawa")
                .zipCode("33-333")
                .email("tytus@com.pl")
                .build();

        PlaceOrderCommand command = PlaceOrderCommand.
                builder()
                .recipient(recipient)
                .item( new OrderItem(montecassino,12 ))
                .item( new OrderItem(Wiersze,6 ))
                .build();

        PlaceOrderResponse response = placeOrder.placeOrder(command);
        System.out.println( "Created ORDER with id: "+ response.getOrderId() );
        queryOrder.findAll()
                .forEach( order -> {
                    System.out.println( "GOT ORDER WITH TOTAL PRICE: " + order.totalPrice() + " DETAIL: "+order);
                } );
    }

    private void serachCatalog() {
        findByTitle();
        findAndUpdate();
        findByTitle();
    }

    private void initDate() {
        catalog.addBook(new CreateBookCommand("Via Carpatia", "Ziemowit Szczerek", 1834, new BigDecimal("19.90")));
        catalog.addBook(new CreateBookCommand("Boże igrzysko", "Norman Davis", 1884, new BigDecimal("29.90")));
        catalog.addBook(new CreateBookCommand("Monte Cassino", "Melchior Wańkowicz", 1960, new BigDecimal("9.90")));
        catalog.addBook(new CreateBookCommand("Na tropach smętka", "Melchior Wańkowicz", 1936, new BigDecimal("1.90")));
        catalog.addBook(new CreateBookCommand("Jadąc do Badadag", "Andrzej Stasiuk", 2000, new BigDecimal("190.90")));
        catalog.addBook(new CreateBookCommand("Wiersze", "Juliusz Słowacki", 1850, new BigDecimal("39.00")));
        catalog.addBook(new CreateBookCommand("Wiersze", "Wisława Szymborska", 2000, new BigDecimal("10.25")));
    }

    private void findByTitle() {
        List<Book> booksByTitle = catalog.findByTitle(title);
        booksByTitle.stream().limit(limit).forEach(System.out::println);
    }

    private void findAndUpdate() {
        System.out.println("Updating book : ");
        catalog.findOneByTitleAndAuthor("Wiersze", "Wisława")
                .ifPresent(book -> {

                    UpdateBookCommand command = UpdateBookCommand.builder()
                            .id(book.getId())
                            .title("Wiersze wybrane")
                            .build();

                    /* Old version
                    UpdateBookCommand command = new UpdateBookCommand(
                            book.getId(),
                            "Wiersze wybrane",
                            book.getAuthor(),
                            book.getYear());
                            */
                    UpdateBookResponse response = catalog.updateBook(command);
                    System.out.println( "Updeting book result : " + response.isSuccess());
                });
    }

}
