package rombuulean.buuleanBook.order.application.port;

import rombuulean.buuleanBook.order.domain.Order;

import java.util.List;

public interface QueryOrderUseCase {
    List<Order> findall();
}
