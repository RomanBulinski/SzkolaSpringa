package rombuulean.buuleanBook.order.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import rombuulean.buuleanBook.catalog.application.port.CatalogUseCase;
import rombuulean.buuleanBook.catalog.domain.Book;
import rombuulean.buuleanBook.catalog.web.CatalogController;
import rombuulean.buuleanBook.order.application.QueryOrderService;
import rombuulean.buuleanBook.order.application.port.QueryOrderUseCase;
import rombuulean.buuleanBook.order.domain.Order;
import rombuulean.buuleanBook.order.domain.OrderItem;
import rombuulean.buuleanBook.order.domain.OrderStatus;
import rombuulean.buuleanBook.order.domain.Recipient;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class OrdersController {

    private final QueryOrderService queryOrderService;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<Order> getAllOrders() {
        return queryOrderService.findAll();
    }


    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Order> getById(@PathVariable Long id) {
        if (id.equals(42L)) {
            throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT, "I am a teapot");
        }
        return queryOrderService
                .findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        queryOrderService.removeById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Order> addOrder(@RequestBody OrdersController.RestOrderCommand command) {
        Order order = queryOrderService.addOrder(command.toCreateCommand());
        return ResponseEntity.created(createdOrderuri(order)).build();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateBook(@PathVariable Long id, @RequestBody OrdersController.RestOrderCommand command) {
        QueryOrderUseCase.UpdateOrderResponse response = queryOrderService.updateOrder(command.toUpdateCommand(id));
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
        private OrderStatus status =  OrderStatus.NEW;
        private List<OrderItem>  items;
        private Recipient recipient;
        private LocalDateTime createdAt = LocalDateTime.now();

        QueryOrderUseCase.CreateOrderCommand toCreateCommand() {
            return new QueryOrderUseCase.CreateOrderCommand(status, items, recipient, createdAt);
        }

        QueryOrderUseCase.UpdateOrderCommand toUpdateCommand(Long id) {
            return new QueryOrderUseCase.UpdateOrderCommand(id, status, items, recipient, createdAt);
        }


    }
}
