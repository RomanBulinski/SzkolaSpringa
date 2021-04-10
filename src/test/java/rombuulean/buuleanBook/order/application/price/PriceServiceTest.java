package rombuulean.buuleanBook.order.application.price;

import org.junit.jupiter.api.Test;
import rombuulean.buuleanBook.catalog.domain.Book;
import rombuulean.buuleanBook.order.domain.Order;
import rombuulean.buuleanBook.order.domain.OrderItem;

import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class PriceServiceTest {

    PriceService priceService = new PriceService();

    @Test
    public void calculateTotalPriceOfEmptyOrder(){
        //given
        Order order = Order.builder().build();
        //when
        OrderPrice orderPrice = priceService.calculatePrice(order);
        //then
        assertEquals(BigDecimal.ZERO, orderPrice.finalPrice());
    }


    @Test
    public void calculateTotalPrice(){
        //given
        Book book1 = new Book();
        book1.setPrice(new BigDecimal("12.50"));
        Book book2 = new Book();
        book2.setPrice(new BigDecimal("33.99"));

        Order order = Order
                .builder()
                .item(new OrderItem(book1,2))
                .item(new OrderItem(book2,5))
                .build();
        //when
        OrderPrice price  = priceService.calculatePrice(order);
        //then
        assertEquals(new BigDecimal("194.95"), price.finalPrice());
        assertEquals(new BigDecimal("194.95"), price.getItemsPrice());
    }
}
