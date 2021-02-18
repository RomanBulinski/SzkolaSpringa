package rombuulean.buuleanBook.order.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import rombuulean.buuleanBook.order.application.port.QueryOrderUseCase;
import rombuulean.buuleanBook.order.domain.Order;
import rombuulean.buuleanBook.order.domain.OrderRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class QueryOrderService implements QueryOrderUseCase {

    private OrderRepository orderRepository;

    @Override
    public List<Order> findall() {
        return orderRepository.findAll();
    }
}
