package rombuulean.buuleanBook.order.application.price;

import rombuulean.buuleanBook.order.domain.Order;

import java.math.BigDecimal;

public class DeliveryDiscountStrategy implements DiscountStrategy {
    @Override
    public BigDecimal calculate(Order order) {
        return null;
    }
}
