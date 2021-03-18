package rombuulean.buuleanBook.order.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import rombuulean.buuleanBook.jpa.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

//@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table( name="orders" )
@EntityListeners(AuditingEntityListener.class)
public class Order extends BaseEntity {

//    @Id
//    @GeneratedValue()
//    private Long id;

//    @Builder.Default
    @Enumerated(value = EnumType.STRING)
    private OrderStatus status = OrderStatus.NEW;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name="order_id")
    private List<OrderItem> items;

    @ManyToOne(cascade = CascadeType.ALL)
    private Recipient recipient;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Order(OrderStatus status, List<OrderItem> items, Recipient recipient, LocalDateTime createdAt ) {
        this.status = status;
        this.items = items;
        this.recipient = recipient;
        this.createdAt = createdAt;
    }

    public Order( List<OrderItem> items, Recipient recipient) {
        this.items = items;
        this.recipient = recipient;
    }

//    public BigDecimal totalPrice() {
//        return items.stream().map(item -> item.getBookId().getPrice().multiply(new BigDecimal( item.getQuantity())))
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//    }

}
