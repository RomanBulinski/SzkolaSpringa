package rombuulean.buuleanBook.order.domain;

import lombok.*;
import rombuulean.buuleanBook.catalog.domain.Book;
import rombuulean.buuleanBook.jpa.BaseEntity;

import javax.persistence.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
    private int quantity;

}
