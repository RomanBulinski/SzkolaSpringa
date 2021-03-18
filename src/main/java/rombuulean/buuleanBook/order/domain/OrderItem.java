package rombuulean.buuleanBook.order.domain;

import lombok.*;
import rombuulean.buuleanBook.jpa.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem extends BaseEntity {
//    @Id
//    @GeneratedValue
//    private Long id;
    private Long bookId;
    private int quantity;

//    public OrderItem(Long bookId, int quantity) {
//        this.bookId = bookId;
//        this.quantity = quantity;
//    }
}
