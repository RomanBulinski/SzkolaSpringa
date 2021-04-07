package rombuulean.buuleanBook.order.application;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import rombuulean.buuleanBook.catalog.application.CatalogService;
import rombuulean.buuleanBook.catalog.application.port.CatalogUseCase;
import rombuulean.buuleanBook.catalog.db.BookJpaRepository;
import rombuulean.buuleanBook.catalog.domain.Book;
import rombuulean.buuleanBook.order.application.port.ManipulateOrderUseCase;
import rombuulean.buuleanBook.order.application.port.ManipulateOrderUseCase.PlaceOrderResponse;
import rombuulean.buuleanBook.order.application.port.ManipulateOrderUseCase.UpdateStatusCommand;
import rombuulean.buuleanBook.order.application.port.QueryOrderUseCase;
import rombuulean.buuleanBook.order.domain.OrderStatus;
import rombuulean.buuleanBook.order.domain.Recipient;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static rombuulean.buuleanBook.order.application.port.ManipulateOrderUseCase.OrderItemCommand;
import static rombuulean.buuleanBook.order.application.port.ManipulateOrderUseCase.PlaceOrderCommand;

//@DataJpaTest
//@Import({ManipulateOrderService.class, CatalogService.class})
@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class OrderServiceTest {

    @Autowired
    BookJpaRepository bookJpaRepository;
    @Autowired
    ManipulateOrderService service;
    @Autowired
    QueryOrderUseCase queryOrderService;
    @Autowired
    CatalogUseCase catalogUseCase;

    @Test
    public void userCanPlaceOrder() {

        //given
        Book effectiveJava = givenJavaConcurrency(50);
        Book jcip = givenEffectiveJava(50);
        PlaceOrderCommand command = PlaceOrderCommand
                .builder()
                .recipient(recipient())
                .item(new OrderItemCommand(effectiveJava.getId(), 10))
                .item(new OrderItemCommand(jcip.getId(), 15))
                .build();
        //when
        PlaceOrderResponse response = service.placeOrder(command);
        //then
        assertTrue(response.isSuccess());
        assertEquals(40L, availableCopiesOf(effectiveJava));
        assertEquals(35L, availableCopiesOf(jcip));
    }

    @Test
    public void userCanRevokeOrder(){
        //given
        Book effectiveJava = givenJavaConcurrency(50L);
        String recipient = "marek@example.org";
        Long orderId = placeOrder(effectiveJava.getId(), 15, recipient);
        assertEquals(35L,availableCopiesOf(effectiveJava));
        //when
        UpdateStatusCommand command = new UpdateStatusCommand(orderId, OrderStatus.CANCELED,recipient );
        service.updateOrderStatus(command);
        //then
        assertEquals(50L,availableCopiesOf(effectiveJava));
        assertEquals(OrderStatus.CANCELED, queryOrderService.findById(orderId).get().getStatus());
    }

    @Disabled("homework")
    public void userCannotRevokePaidOrder(){
        // user ni emoze ywcofac juz oplaconego zamowienia
    }

    @Disabled("homework")
    public void userCannotRevokeShippedOrder(){
        // user ni emoze ywcofac juz wyslanego zamowienia
    }

    @Disabled("homework")
    public void userCannotOrderNoExistingBooks(){
        // user ni emoze zamowic nieistniejacej ksiazki
    }

    @Disabled("homework")
    public void userCannotOrderNegativNumberOfBooks(){
        // user nie moze ujemnej liczbu ksiazek
    }

    @Test
    public void userCannotRevokeOtherUserOrder() {
        //given
        Book effectiveJava = givenJavaConcurrency(50L);
        String adam = "adam@exaple.org";
        Long orderId = placeOrder(effectiveJava.getId(), 15, adam);
        assertEquals(35L,availableCopiesOf(effectiveJava));
        //when
        UpdateStatusCommand command = new UpdateStatusCommand(orderId, OrderStatus.CANCELED,"marek@example.org" );
        service.updateOrderStatus(command);
        //then
        assertEquals(35L,availableCopiesOf(effectiveJava));
        assertEquals(OrderStatus.NEW, queryOrderService.findById(orderId).get().getStatus());
    }

    @Test
    public void userCantOrderMoreBooksThanAvailable() {

        //given
        Book effectiveJava = givenJavaConcurrency(5);
        Book jcip = givenEffectiveJava(50);
        PlaceOrderCommand command = PlaceOrderCommand
                .builder()
                .recipient(recipient())
                .item(new OrderItemCommand(effectiveJava.getId(), 10))
                .item(new OrderItemCommand(jcip.getId(), 10))
                .build();
        //when
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            service.placeOrder(command);
        });
        //then
        assertTrue(exception.getMessage().contains("Too many copies of book " + effectiveJava.getId() + " requested: 10 of 5 available"));
    }

    private Long placeOrder(Long bookId, int copies, String recipient){
        PlaceOrderCommand command = PlaceOrderCommand
                .builder()
                .recipient( recipient(recipient) )
                .item(new OrderItemCommand(bookId, copies))
                .build();
        return service.placeOrder(command).getRight();
    }

    private Long placeOrder(Long bookId, int copies ){
        return placeOrder( bookId, copies , "jhon@example.org"  );
    }

    private Book givenJavaConcurrency(long available) {
        return bookJpaRepository.save(new Book("Java Concurrency in Practice", 2006, new BigDecimal("90.90"), available));
    }

    private Book givenEffectiveJava(long available) {
        return bookJpaRepository.save(new Book("Effective Java", 2006, new BigDecimal("90.90"), available));
    }

    private Recipient recipient() {
        return Recipient.builder().email("jhon@example.org").build();
    }

    private Recipient recipient(String email) {
        return Recipient.builder().email(email).build();
    }

    private Long availableCopiesOf(Book book) {
        return catalogUseCase.findById(book.getId()).get().getAvailable();
    }
}
