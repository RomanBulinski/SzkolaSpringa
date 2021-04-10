package rombuulean.buuleanBook.order.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import rombuulean.buuleanBook.order.application.port.QueryOrderUseCase;
import rombuulean.buuleanBook.order.application.price.OrderPrice;
import rombuulean.buuleanBook.order.application.price.PriceService;
import rombuulean.buuleanBook.order.db.OrderJpaRepository;
import rombuulean.buuleanBook.order.domain.Order;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class QueryOrderService implements QueryOrderUseCase {

    private final OrderJpaRepository orderRepository;
    private final PriceService priceService;

    @Override
    public List<RichOrder> findAll() {
        return orderRepository.findAll()
                .stream()
                .map(this::toRichOrder)
                .collect(Collectors.toList());
    }

//    @Override
//    public List<Order> findAll() {
//        return orderRepository.findAll();
//    }

    @Override
    public Optional<RichOrder> findById(Long id) {
        return orderRepository.findById(id).map(this::toRichOrder);
    }

    private RichOrder toRichOrder(Order order){
        OrderPrice orderPrice = priceService.calculatePrice(order);
        return new RichOrder(
                order.getId(),
                order.getStatus(),
                order.getItems(),
                order.getRecipient(),
                order.getCreatedAt(),
                orderPrice,
                orderPrice.finalPrice()
        );
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
