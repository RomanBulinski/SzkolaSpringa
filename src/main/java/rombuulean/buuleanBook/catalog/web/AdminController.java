package rombuulean.buuleanBook.catalog.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rombuulean.buuleanBook.catalog.application.port.CatalogUseCase;
import rombuulean.buuleanBook.catalog.db.AuthorJpaRepository;
import rombuulean.buuleanBook.catalog.domain.Author;
import rombuulean.buuleanBook.catalog.domain.Book;
import rombuulean.buuleanBook.order.application.port.ManipulateOrderUseCase;
import rombuulean.buuleanBook.order.application.port.QueryOrderUseCase;
import rombuulean.buuleanBook.order.domain.OrderItem;
import rombuulean.buuleanBook.order.domain.Recipient;

import java.math.BigDecimal;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {

    private final CatalogUseCase catalog;
    private final ManipulateOrderUseCase placeOrder;
    private final QueryOrderUseCase queryOrder;
    private final AuthorJpaRepository authorJpaRepository;

    @PostMapping("/data")
    @Transactional
    public void initialize() {
        initDate();
        placeOrder();
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
        log.info( result );

        queryOrder.findAll()
                .forEach(order -> {
                    log.info("GOT ORDER WITH TOTAL PRICE: " + " DETAIL: " + order);
                });
    }

    private void initDate() {

        Author joshua = new Author("Joshua", "Bloch");
        Author neal = new Author("Neal", "Gafter");
        authorJpaRepository.save(joshua);
        authorJpaRepository.save(neal);

        CatalogUseCase.CreateBookCommand effectiveJava = new CatalogUseCase.CreateBookCommand(
                "Effective Java 3",
                Set.of(joshua.getId(),
                        neal.getId()), 2005,
                new BigDecimal("59.90"),
                50L
        );
        CatalogUseCase.CreateBookCommand javaPuzzlers = new CatalogUseCase.CreateBookCommand(
                "Java Puzzlers 2",
                Set.of(joshua.getId()),
                2018,
                new BigDecimal("89.00"),
                50L);
        catalog.addBook(effectiveJava);
        catalog.addBook(javaPuzzlers);
    }

}

