package rombuulean.buuleanBook.order.domain;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class Order {

    private Long id;
    private OrderStatus status;
    private List<OrderItem> items;
    private Recipient recipient;
    private LocalDateTime createdAt;

    public BigDecimal totalPrice() {
        return items.stream().map(item -> item.getBook().getPrice().multiply(new BigDecimal( item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void setId(long nextId) {
    }
}
