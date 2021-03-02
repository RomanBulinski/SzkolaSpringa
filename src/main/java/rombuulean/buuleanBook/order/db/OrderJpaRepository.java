package rombuulean.buuleanBook.order.db;

import org.springframework.data.jpa.repository.JpaRepository;
import rombuulean.buuleanBook.order.domain.Order;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {

}
