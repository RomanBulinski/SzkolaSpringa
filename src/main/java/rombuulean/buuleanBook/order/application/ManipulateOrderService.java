package rombuulean.buuleanBook.order.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rombuulean.buuleanBook.catalog.db.BookJpaRepository;
import rombuulean.buuleanBook.catalog.domain.Book;
import rombuulean.buuleanBook.order.application.port.ManipulateOrderUseCase;
import rombuulean.buuleanBook.order.db.OrderJpaRepository;
import rombuulean.buuleanBook.order.db.RecipientJpaRepository;
import rombuulean.buuleanBook.order.domain.Order;
import rombuulean.buuleanBook.order.domain.OrderItem;
import rombuulean.buuleanBook.order.domain.OrderStatus;
import rombuulean.buuleanBook.order.domain.Recipient;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
class ManipulateOrderService implements ManipulateOrderUseCase {
    private final OrderJpaRepository repository;
    private final BookJpaRepository bookJpaRepository;
    private final RecipientJpaRepository recipientJpaRepository;

    @Override
    public PlaceOrderResponse placeOrder(PlaceOrderCommand command) {
        Set<OrderItem> items = command.getItems()
                .stream()
                .map(this::toOrderItem)
                .collect(Collectors.toSet());
        Order order = Order
                .builder()
                .status(OrderStatus.NEW)
                .recipient(getOrCreateRecipient(command.getRecipient()))
                .items(items)
                .build();
        Order save = repository.save(order);
        bookJpaRepository.saveAll(updateBooks(items));
        return PlaceOrderResponse.success(save.getId());
    }

    private Recipient getOrCreateRecipient(Recipient recipient) {
        return recipientJpaRepository
                .findByEmailIgnoreCase(recipient.getEmail())
                .orElse(recipient);
    }

    private Set<Book> updateBooks(Set<OrderItem> items) {
        return items.stream().map(item -> {
            Book book = item.getBook();
            book.setAvailable(book.getAvailable() - item.getQuantity());
            return book;
        }).collect(Collectors.toSet());
    }


    private OrderItem toOrderItem(OrderItemCommand command) {
        Book book = bookJpaRepository.getOne(command.getBookId());
        int quantity = command.getQuantity();
        if (book.getAvailable() >= quantity) {
            return new OrderItem(book, command.getQuantity());
        }
        throw new IllegalArgumentException("Too many copies pof book "
                + book.getId()
                + " requested: " + quantity + " of " + book.getAvailable() + " available ");
    }

    @Override
    public void deleteOrderById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void updateOrderStatus(Long id, OrderStatus status) {
        repository.findById(id)
                .ifPresent(order -> {
                    order.getStatus().updateStatus(status);
                    order.setStatus(status);
                    repository.save(order);
                });
    }


}
