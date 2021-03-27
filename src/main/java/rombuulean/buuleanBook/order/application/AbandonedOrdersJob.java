package rombuulean.buuleanBook.order.application;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import rombuulean.buuleanBook.order.application.port.ManipulateOrderUseCase;
import rombuulean.buuleanBook.order.db.OrderJpaRepository;
import rombuulean.buuleanBook.order.domain.Order;
import rombuulean.buuleanBook.order.domain.OrderStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Component
@AllArgsConstructor
public class AbandonedOrdersJob {
    private final OrderJpaRepository repository;
    private final ManipulateOrderUseCase orderUseCase;
    private final OrdersProperties properties;

    @Transactional
    @Scheduled(cron = "${app.orders.abandon-crone}")
    public void run(){

        Duration paymentPeriod = properties.getPaymentPeriod();
        LocalDateTime olderThen = LocalDateTime.now().minus(paymentPeriod);
        List<Order> orders =   repository.findByStatusAndCreatedAtLessThanEqual(OrderStatus.NEW, olderThen);
        System.out.println("Founded orders to be abandoned: " + orders.size());
        orders.forEach( order -> orderUseCase.updateOrderStatus(order.getId(), OrderStatus.ABANDONED));

    }

}
