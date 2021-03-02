package rombuulean.buuleanBook.order.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import rombuulean.buuleanBook.order.application.port.QueryOrderUseCase;
import rombuulean.buuleanBook.order.db.OrderJpaRepository;
import rombuulean.buuleanBook.order.domain.Order;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class QueryOrderService implements QueryOrderUseCase {

    private OrderJpaRepository orderRepository;

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public void removeById(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public Order addOrder(CreateOrderCommand command) {
        Order order = command.toOrder();
        return orderRepository.save(order);
    }

    @Override
    public UpdateOrderResponse updateOrder(UpdateOrderCommand command) {
        return orderRepository
                .findById(command.getId())
                .map(order -> {
                    Order updatedOrder = command.updateFields(order);
                    orderRepository.save(updatedOrder);
                    return QueryOrderUseCase.UpdateOrderResponse.SUCCESS;
                })
                .orElseGet(() -> new QueryOrderUseCase.UpdateOrderResponse(false, Arrays.asList("Order not found with id: " + command.getId())));
    }


}
