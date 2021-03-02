package rombuulean.buuleanBook.order.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import rombuulean.buuleanBook.order.application.port.PlaceOrderUseCase;
import rombuulean.buuleanBook.order.db.OrderJpaRepository;
import rombuulean.buuleanBook.order.domain.Order;

@Service
@AllArgsConstructor
public class PlaceOrderUseService implements PlaceOrderUseCase {
    private final OrderJpaRepository repository;

    @Override
    public PlaceOrderResponse placeOrder(PlaceOrderCommand command) {

//        Order order = Order.builder()
//                .recipient(command.getRecipient())
//                .items(command.getItem())
//                .build();

        Order order = new Order(command.getItem(),command.getRecipient() );
        Order save = repository.save(order);
        return PlaceOrderResponse.success(save.getId());
    }

}
