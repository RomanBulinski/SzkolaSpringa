package rombuulean.buuleanBook.order.application;

import lombok.Value;
import rombuulean.buuleanBook.order.application.price.OrderPrice;
import rombuulean.buuleanBook.order.domain.OrderItem;
import rombuulean.buuleanBook.order.domain.OrderStatus;
import rombuulean.buuleanBook.order.domain.Recipient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Value
public class RichOrder {
    Long id;
    OrderStatus status;
    Set<OrderItem> items;
    Recipient recipient;
    LocalDateTime createdAt;
    OrderPrice orderPrice;
    BigDecimal finalPrice;

}
