package rombuulean.buuleanBook.catalog.application;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rombuulean.buuleanBook.catalog.application.port.CatalogInitializerUseCase;
import rombuulean.buuleanBook.catalog.application.port.CatalogUseCase;
import rombuulean.buuleanBook.catalog.db.AuthorJpaRepository;
import rombuulean.buuleanBook.catalog.domain.Book;
import rombuulean.buuleanBook.order.application.port.ManipulateOrderUseCase;
import rombuulean.buuleanBook.order.application.port.QueryOrderUseCase;
import rombuulean.buuleanBook.order.domain.Recipient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class CatalogInitializerService implements CatalogInitializerUseCase {

    private final CatalogUseCase catalog;
    private final ManipulateOrderUseCase placeOrder;
    private final QueryOrderUseCase queryOrder;
    private final AuthorJpaRepository authorJpaRepository;

    @Override
    @Transactional
    public void initialize() {
        initDate();
        placeOrder();
    }

    private void initDate() {
        ClassPathResource resource = new ClassPathResource("books.csv");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new ClassPathResource("book.csv").getInputStream()))) {
            CsvToBean<CsvBook> build = new CsvToBeanBuilder<CsvBook>(reader)
                    .withType(CsvBook.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            build.stream().forEach(this::initBook);

        } catch (IOException e) {
            throw new IllegalStateException("Failed to parse CSV file", e);
        }
    }

    private void initBook(CsvBook csvBook) {
        //parsw authors
        CatalogUseCase.CreateBookCommand createBookCommand = new CatalogUseCase.CreateBookCommand(
                csvBook.title,
                Set.of(),
                csvBook.year,
                csvBook.amount,
                50L
        );
        catalog.addBook(createBookCommand);
        //upload thumbnail
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CsvBook {
        @CsvBindByName
        private String title;
        @CsvBindByName
        private String authors;
        @CsvBindByName
        private String year;
        @CsvBindByName
        private String amount;
        @CsvBindByName
        private String thumbnail;
    }


    private void placeOrder() {
        Book effectivejava = catalog.findOneByTitle("Effective Java").orElseThrow(() -> new IllegalStateException("Cannot find a book"));
        Book puzzlers = catalog.findOneByTitle("Java Puzzlers").orElseThrow(() -> new IllegalStateException("Cannot find a book"));

        Recipient recipient = Recipient.builder()
                .name("Tytus Kowalski")
                .phone(("333-333-333-33"))
                .street("Warszawska")
                .city("Warszawa")
                .zipCode("33-333")
                .email("tytus@com.pl")
                .build();

        ManipulateOrderUseCase.PlaceOrderCommand command = ManipulateOrderUseCase.PlaceOrderCommand.
                builder()
                .recipient(recipient)
                .item(new ManipulateOrderUseCase.OrderItemCommand(effectivejava.getId(), 12))
                .item(new ManipulateOrderUseCase.OrderItemCommand(puzzlers.getId(), 6))
                .build();


        ManipulateOrderUseCase.PlaceOrderResponse response = placeOrder.placeOrder(command);
        String result = response.handle(
                orderId -> "Created ORDER with id: " + orderId,
                error -> "Failed to created order: " + error
        );
        log.info(result);

        queryOrder.findAll()
                .forEach(order -> {
                    log.info("GOT ORDER WITH TOTAL PRICE: " + " DETAIL: " + order);
                });
    }

}
