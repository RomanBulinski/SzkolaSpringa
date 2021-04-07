package rombuulean.buuleanBook.order.application;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
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
        log.info("Founded orders to be abandoned: " + orders.size());
        orders.forEach( order -> {
            //TODO Naprawic w module security
            ManipulateOrderUseCase.UpdateStatusCommand command = new ManipulateOrderUseCase.UpdateStatusCommand(order.getId(), OrderStatus.NEW,null );
            orderUseCase.updateOrderStatus(command);
        });

    }

}
