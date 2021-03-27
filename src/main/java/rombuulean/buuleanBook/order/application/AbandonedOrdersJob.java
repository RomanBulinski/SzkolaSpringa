package rombuulean.buuleanBook.order.application;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import rombuulean.buuleanBook.order.application.port.ManipulateOrderUseCase;
import rombuulean.buuleanBook.order.db.OrderJpaRepository;
import rombuulean.buuleanBook.order.domain.Order;
import rombuulean.buuleanBook.order.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

@Component
@AllArgsConstructor
public class AbandonedOrdersJob {
    private final OrderJpaRepository repository;
    private final ManipulateOrderUseCase orderUseCase;

    @Transactional
    @Scheduled(fixedRate = 60000)
    public void run(){
        LocalDateTime timestamp = LocalDateTime.now().minusMinutes(5);
        List<Order> orders =   repository.findByStatusAndCreatedAtLessThanEqual(OrderStatus.NEW, timestamp);
        System.out.println("Founded orders to be abandoned: " + orders.size());
        orders.forEach( order -> orderUseCase.updateOrderStatus(order.getId(), OrderStatus.ABANDONED));
    }

}
