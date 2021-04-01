package rombuulean.buuleanBook.order.application;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import rombuulean.buuleanBook.catalog.db.BookJpaRepository;
import rombuulean.buuleanBook.catalog.domain.Book;
import rombuulean.buuleanBook.order.application.port.ManipulateOrderUseCase;
import rombuulean.buuleanBook.order.domain.Recipient;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import({ManipulateOrderService.class})
class ManipulateOrderServiceTest {

    @Autowired
    BookJpaRepository bookJpaRepository;
    @Autowired
    ManipulateOrderService service;

    @Test
    public void userCanPlaceOrder() {

        //given
        Book  effectiveJava = givenJavaConcurrency(50);
        Book  jcip = givenEffectiveJava(50);
        ManipulateOrderUseCase.PlaceOrderCommand command = ManipulateOrderUseCase.PlaceOrderCommand
                .builder()
                .recipient(recipient() )
                .item( new ManipulateOrderUseCase.OrderItemCommand(effectiveJava.getId(), 10))
                .item( new ManipulateOrderUseCase.OrderItemCommand(jcip.getId(), 10))
                .build();
        //when
        ManipulateOrderUseCase.PlaceOrderResponse response = service.placeOrder(command);
        //then
        assertTrue(response.isSuccess());

    }


    private Book givenJavaConcurrency(long available){
        return bookJpaRepository.save(new Book("Java Concurrency in Practice",2006, new BigDecimal("90.90"),available));
    }

    private Book givenEffectiveJava(long available){
        return bookJpaRepository.save(new Book("Effective Java",2006, new BigDecimal("90.90"),available));
    }

    private Recipient recipient(){
        return Recipient.builder().email("jhon@example.org").build();
    }
}
