package rombuulean.buuleanBook.order.application.port;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import rombuulean.buuleanBook.catalog.application.port.CatalogUseCase;
import rombuulean.buuleanBook.catalog.domain.Book;
import rombuulean.buuleanBook.order.domain.Order;
import rombuulean.buuleanBook.order.domain.OrderItem;
import rombuulean.buuleanBook.order.domain.OrderStatus;
import rombuulean.buuleanBook.order.domain.Recipient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

public interface QueryOrderUseCase {
    List<Order> findAll();
    Optional<Order> findById(Long id);
    void removeById(Long id);
    Order addOrder(QueryOrderUseCase.CreateOrderCommand command);
    QueryOrderUseCase.UpdateOrderResponse updateOrder(QueryOrderUseCase.UpdateOrderCommand command);

    @Value
    class CreateOrderCommand {
        OrderStatus status;
        List<OrderItem> items;
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
        List<OrderItem> items;
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
        public static QueryOrderUseCase.UpdateOrderResponse SUCCESS = new QueryOrderUseCase.UpdateOrderResponse(true, emptyList());
        boolean success;
        List<String> errors;

    }

}
