package rombuulean.buuleanBook.order.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import rombuulean.buuleanBook.order.application.port.QueryOrderUseCase;
import rombuulean.buuleanBook.order.domain.Order;
import rombuulean.buuleanBook.order.domain.OrderItem;
import rombuulean.buuleanBook.order.domain.OrderStatus;
import rombuulean.buuleanBook.order.domain.Recipient;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

import static rombuulean.buuleanBook.order.application.port.QueryOrderUseCase.*;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class OrdersController {

    private final QueryOrderUseCase queryOrderUseCase;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<Order> getAllOrders() {
        return queryOrderUseCase.findAll();
    }


    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Order> getById(@PathVariable Long id) {
        if (id.equals(42L)) {
            throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT, "I am a teapot");
        }
        return queryOrderUseCase
                .findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        queryOrderUseCase.removeById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Order> addOrder(@RequestBody RestOrderCommand restOrderCommand) {
        Order order = queryOrderUseCase.addOrder(restOrderCommand.toCreateCommand());
        return ResponseEntity.created(createdOrderuri(order)).build();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateBook(@PathVariable Long id, @RequestBody RestOrderCommand restOrderCommand) {
        UpdateOrderResponse response = queryOrderUseCase.updateOrder(restOrderCommand.toUpdateCommand(id));
        if (!response.isSuccess()) {
            String message = String.join(", ", response.getErrors());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }
    }

    private URI createdOrderuri(Order order) {
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/" + order.getId().toString()).build().toUri();
        return uri;
    }

    @Data
    private static class RestOrderCommand {
        private OrderStatus status = OrderStatus.NEW;
        private List<OrderItem> items;
        private Recipient recipient;
        private LocalDateTime createdAt = LocalDateTime.now();

        CreateOrderCommand toCreateCommand() {
            return new CreateOrderCommand(status, items, recipient, createdAt);
        }

        UpdateOrderCommand toUpdateCommand(Long id) {
            return new UpdateOrderCommand(id, status, items, recipient, createdAt);
        }

    }
}
