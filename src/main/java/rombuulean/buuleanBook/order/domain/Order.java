package rombuulean.buuleanBook.order.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import rombuulean.buuleanBook.jpa.BaseEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class Order extends BaseEntity {

    @Builder.Default
    @Enumerated(value = EnumType.STRING)
    private OrderStatus status = OrderStatus.NEW;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private Set<OrderItem> items;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Recipient recipient;

    @Builder.Default
    @Enumerated(value = EnumType.STRING)
    private Delivery delivery = Delivery.COURIER;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Order(OrderStatus status, Set<OrderItem> items, Recipient recipient, LocalDateTime createdAt) {
        this.status = status;
        this.items = items;
        this.recipient = recipient;
        this.createdAt = createdAt;
    }

    public UpdateStatusResult updateStatus(OrderStatus newStatus){
        UpdateStatusResult result = this.status.updateStatus(newStatus);
        this.status = result.getNewStatus();
        return result;
    }

    public BigDecimal getItemsPrice() {
        return items.stream()
                .map(item -> item.getBook().getPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getDeliveryPrice(){
        return delivery.getPrice();
    }

}
