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
import rombuulean.buuleanBook.order.domain.Recipient;
import rombuulean.buuleanBook.order.domain.UpdateStatusResult;
import rombuulean.buuleanBook.security.UserSecurity;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
class ManipulateOrderService implements ManipulateOrderUseCase {
    private final OrderJpaRepository repository;
    private final BookJpaRepository bookJpaRepository;
    private final RecipientJpaRepository recipientJpaRepository;
    private final UserSecurity userSecurity;

    @Override
    public PlaceOrderResponse placeOrder(PlaceOrderCommand command) {
        Set<OrderItem> items = command.getItems()
                .stream()
                .map(this::toOrderItem)
                .collect(Collectors.toSet());
        Order order = Order
                .builder()
                .recipient(getOrCreateRecipient(command.getRecipient()))
                .delivery(command.getDelivery())
                .items(items)
                .build();
        Order save = repository.save(order);
        bookJpaRepository.saveAll(reduceBooks(items));
        return PlaceOrderResponse.success(save.getId());
    }

    private Recipient getOrCreateRecipient(Recipient recipient) {
        return recipientJpaRepository
                .findByEmailIgnoreCase(recipient.getEmail())
                .orElse(recipient);
    }

    private Set<Book> reduceBooks(Set<OrderItem> items) {
        return items.stream().map(item -> {
            Book book = item.getBook();
            book.setAvailable(book.getAvailable() - item.getQuantity());
            return book;
        }).collect(Collectors.toSet());
    }


    private OrderItem toOrderItem(OrderItemCommand command) {
        Book book = bookJpaRepository.getOne(command.getBookId());
        int quantity = command.getQuantity();
        checkIfBookExistInDB(command);
        checkIfQuantityNoNegative(quantity);
        checkIfQuantityLessThenAvailable(book,quantity);
        return new OrderItem(book, command.getQuantity());
    }

    private void checkIfQuantityLessThenAvailable(Book book,int quantity ) {
        if (book.getAvailable() < quantity) {
            throw new IllegalArgumentException("Too many copies of book "
                    + book.getId()
                    + " requested: " + quantity + " of " + book.getAvailable() + " available ");
        }
    }

    private void checkIfQuantityNoNegative(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Attention. Quantity less then 0");
        }
    }

    private void checkIfBookExistInDB(OrderItemCommand command) {
        Optional<Book> checkingBook = bookJpaRepository.findById(command.getBookId());
        if (checkingBook.isEmpty()) {
            throw new IllegalArgumentException("There is no book with id " + command.getBookId());
        }
    }

    @Override
    public void deleteOrderById(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public UpdateStatusResponse updateOrderStatus(UpdateStatusCommand command) {
        return repository.findById(command.getOrderId())
                .map(order -> {
                    if(userSecurity.isOwnerOrAdmin(order.getRecipient().getEmail(), command.getUser())){
                        UpdateStatusResult result = order.updateStatus(command.getStatus());
                        if (result.isRevoked()) {
                            bookJpaRepository.saveAll(revokeBooks(order.getItems()));
                        }
                        repository.save(order);
                        return UpdateStatusResponse.success(order.getStatus());
                    }
                    return UpdateStatusResponse.failure(Error.FORBIDDEN);
                })
                .orElse(UpdateStatusResponse.failure(Error.NOT_FOUND));
    }

    private Set<Book> revokeBooks(Set<OrderItem> items) {
        return items.stream().map(item -> {
            Book book = item.getBook();
            book.setAvailable(book.getAvailable() + item.getQuantity());
            return book;
        }).collect(Collectors.toSet());
    }

}
