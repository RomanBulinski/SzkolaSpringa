package rombuulean.buuleanBook.order.application.price;

import rombuulean.buuleanBook.order.domain.Order;

import java.math.BigDecimal;

public interface DiscountStrategy {
    BigDecimal calculate(Order order);
}
