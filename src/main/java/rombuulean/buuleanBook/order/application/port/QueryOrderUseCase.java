package rombuulean.buuleanBook.order.application.port;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import rombuulean.buuleanBook.order.domain.Order;
import rombuulean.buuleanBook.order.domain.OrderItem;
import rombuulean.buuleanBook.order.domain.OrderStatus;
import rombuulean.buuleanBook.order.domain.Recipient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.Collections.emptyList;

public interface QueryOrderUseCase {
    List<Order> findAll();
    Optional<Order> findById(Long id);
    void removeById(Long id);
    Order addOrder(CreateOrderCommand createOrderCommand);
    UpdateOrderResponse updateOrder(UpdateOrderCommand updateOrderCommand);

    @Value
    class CreateOrderCommand {
        OrderStatus status;
        Set<OrderItem> items;
        Recipient recipient;
        LocalDateTime createdAt;

        public Order toOrder() {
            return new Order(status, items, recipient, createdAt);
        }
    }

    @Value
    @Builder
    @AllArgsConstructor
    class UpdateOrderCommand{
        Long id;
        OrderStatus status;
        Set<OrderItem> items;
        Recipient recipient;
        LocalDateTime createdAt;

        public Order updateFields(Order order) {
            if (status != null) {
                order.setStatus(status);
            }
            if (items != null) {
                order.setItems(items);
            }
            if (recipient != null) {
                order.setRecipient(recipient);
            }
            if (createdAt != null) {
                order.setCreatedAt(createdAt);
            }
            return order;
        }
    }

    @Value
    class UpdateOrderResponse{
        public static UpdateOrderResponse SUCCESS = new UpdateOrderResponse(true, emptyList());
        boolean success;
        List<String> errors;

    }


}
