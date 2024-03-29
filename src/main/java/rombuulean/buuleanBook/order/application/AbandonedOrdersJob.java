package rombuulean.buuleanBook.order.application;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import rombuulean.buuleanBook.clock.Clock;
import rombuulean.buuleanBook.order.application.port.ManipulateOrderUseCase;
import rombuulean.buuleanBook.order.db.OrderJpaRepository;
import rombuulean.buuleanBook.order.domain.Order;
import rombuulean.buuleanBook.order.domain.OrderStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static rombuulean.buuleanBook.order.application.port.ManipulateOrderUseCase.*;

@Slf4j
@Component
@AllArgsConstructor
public class AbandonedOrdersJob {
    private final OrderJpaRepository repository;
    private final ManipulateOrderUseCase orderUseCase;
    private final OrdersProperties properties;
    private final User systemUser;
    private final Clock clock;

    @Transactional
    @Scheduled(cron = "${app.orders.abandon-crone}")
    public void run(){

        Duration paymentPeriod = properties.getPaymentPeriod();
        LocalDateTime olderThen = clock.now().minus(paymentPeriod);
        List<Order> orders =   repository.findByStatusAndCreatedAtLessThanEqual(OrderStatus.NEW, olderThen);
        log.info("Founded orders to be abandoned: " + orders.size());
        orders.forEach( order -> {
            UpdateStatusCommand command = new UpdateStatusCommand(order.getId(), OrderStatus.ABANDONED, systemUser );
            orderUseCase.updateOrderStatus(command);
        });
    }
}
