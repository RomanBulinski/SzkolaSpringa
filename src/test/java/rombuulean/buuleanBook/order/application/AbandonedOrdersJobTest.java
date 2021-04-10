package rombuulean.buuleanBook.order.application;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import rombuulean.buuleanBook.catalog.application.port.CatalogUseCase;
import rombuulean.buuleanBook.catalog.db.BookJpaRepository;
import rombuulean.buuleanBook.catalog.domain.Book;
import rombuulean.buuleanBook.clock.Clock;
import rombuulean.buuleanBook.order.application.port.QueryOrderUseCase;
import rombuulean.buuleanBook.order.domain.OrderStatus;
import rombuulean.buuleanBook.order.domain.Recipient;

import java.math.BigDecimal;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static rombuulean.buuleanBook.order.application.port.ManipulateOrderUseCase.OrderItemCommand;
import static rombuulean.buuleanBook.order.application.port.ManipulateOrderUseCase.PlaceOrderCommand;

@SpringBootTest(properties = "app.orders.payment-period=1H")
@AutoConfigureTestDatabase
class AbandonedOrdersJobTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        public Clock.Fake clock() {
            return new Clock.Fake();
        }
    }

    @Autowired
    CatalogUseCase catalogUseCase;

    @Autowired
    QueryOrderUseCase queryOrderService;

    @Autowired
    BookJpaRepository bookJpaRepository;

    @Autowired
    ManipulateOrderService manipulateOrderService;

    @Autowired
    Clock.Fake clock;

    @Autowired
    AbandonedOrdersJob ordersJob;

    @Test
    public void shouldMarkOrderAsAbandoned() {

        //given - orders
        Book book = givenEffectiveJava(50L);
        Long orderId = placeOrder(book.getId(), 15);
        //when - run
        clock.tick(Duration.ofHours(2));
        ordersJob.run();
        //then - status changed
        assertEquals(OrderStatus.ABANDONED, queryOrderService.findById(orderId).get().getStatus());
        assertEquals(50L, availableCopiesOf(book));

    }

    private Long placeOrder(Long bookId, int copies) {
        PlaceOrderCommand command = PlaceOrderCommand
                .builder()
                .recipient(recipient())
                .item(new OrderItemCommand(bookId, copies))
                .build();
        return manipulateOrderService.placeOrder(command).getRight();
    }

    private Recipient recipient() {
        return Recipient.builder().email("marek@example.org").build();
    }

    private Book givenEffectiveJava(long available) {
        return bookJpaRepository.save(new Book("Effective Java", 2006, new BigDecimal("90.90"), available));
    }

    private Long availableCopiesOf(Book book) {
        return catalogUseCase.findById(book.getId())
                .get()
                .getAvailable();
    }
}
