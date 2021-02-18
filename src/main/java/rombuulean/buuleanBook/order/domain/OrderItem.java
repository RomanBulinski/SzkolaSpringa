package rombuulean.buuleanBook.order.domain;

import lombok.Value;
import rombuulean.buuleanBook.catalog.domain.Book;

@Value
public class OrderItem {

    Book book;
    int quantity;

}
