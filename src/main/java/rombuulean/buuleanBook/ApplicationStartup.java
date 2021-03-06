package rombuulean.buuleanBook;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import rombuulean.buuleanBook.catalog.application.port.CatalogUseCase;
import rombuulean.buuleanBook.catalog.application.port.CatalogUseCase.CreateBookCommand;
import rombuulean.buuleanBook.catalog.db.AuthorJpaRepository;
import rombuulean.buuleanBook.catalog.domain.Author;
import rombuulean.buuleanBook.catalog.domain.Book;
import rombuulean.buuleanBook.order.application.port.PlaceOrderUseCase;
import rombuulean.buuleanBook.order.application.port.PlaceOrderUseCase.PlaceOrderCommand;
import rombuulean.buuleanBook.order.application.port.PlaceOrderUseCase.PlaceOrderResponse;
import rombuulean.buuleanBook.order.application.port.QueryOrderUseCase;
import rombuulean.buuleanBook.order.domain.OrderItem;
import rombuulean.buuleanBook.order.domain.Recipient;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static rombuulean.buuleanBook.catalog.application.port.CatalogUseCase.*;

//@RequiredArgsConstructor
@Component
@AllArgsConstructor
public class ApplicationStartup implements CommandLineRunner {

    private final CatalogUseCase catalog;
    private final PlaceOrderUseCase placeOrder;
    private final QueryOrderUseCase queryOrder;
    private final AuthorJpaRepository authorJpaRepository;

    @Override
    public void run(String... args) {
        initDate();
        placeOrder();
    }

    private void placeOrder() {
        Book effectivejava = catalog.findOneByTitle("Effective Java").orElseThrow(()-> new IllegalStateException("Cannot find a book"));
        Book puzzlers = catalog.findOneByTitle("Java Puzzlers").orElseThrow(()-> new IllegalStateException("Cannot find a book"));

        Recipient recipient = Recipient.builder()
                .name("Tytus Kowalski")
                .phone(("333-333-333-33"))
                .street("Warszawska")
                .city("Warszawa")
                .zipCode("33-333")
                .email("tytus@com.pl")
                .build();

        PlaceOrderCommand command = PlaceOrderCommand.
                builder()
                .recipient(recipient)
//                .item( new OrderItem(montecassino.getId(),12 ))
//                .item( new OrderItem(Wiersze.getId(),6 ))
                .build();

        PlaceOrderResponse response = placeOrder.placeOrder(command);
        System.out.println( "Created ORDER with id: "+ response.getOrderId() );
        queryOrder.findAll()
                .forEach( order -> {
                    System.out.println( "GOT ORDER WITH TOTAL PRICE: " + " DETAIL: "+order);
                    //TODO create method order.totalPrice()
//                    System.out.println( "GOT ORDER WITH TOTAL PRICE: " + order.totalPrice() + " DETAIL: "+order);
                } );
    }


    private void initDate() {

        Author joshua = new Author("Joshua", "Bloch");
        Author neal = new Author("Neal", "Gafter");
        authorJpaRepository.save(joshua);
        authorJpaRepository.save(neal);

        CreateBookCommand  effectiveJava= new CreateBookCommand("Effective Java",  Set.of(joshua.getId(), neal.getId()), 2005, new BigDecimal("59.90"));
        CreateBookCommand  javaPuzzlers= new CreateBookCommand("Java Puzzlers",  Set.of(joshua.getId()), 2018, new BigDecimal("89.00"));

        catalog.addBook(effectiveJava);
        catalog.addBook(javaPuzzlers);

    }

//    private void findByTitle() {
//        List<Book> booksByTitle = catalog.findByTitle(title);
//        booksByTitle.stream().limit(limit).forEach(System.out::println);
//    }

    private void findAndUpdate() {
        System.out.println("Updating book : ");
        catalog.findByTitleAndAuthor("Wiersze", "Wisława")
                .forEach(book -> {

                    UpdateBookCommand command = UpdateBookCommand.builder()
                            .id(book.getId())
                            .title("Wiersze wybrane")
                            .build();

                    /* Old version
                    UpdateBookCommand command = new UpdateBookCommand(
                            book.getId(),
                            "Wiersze wybrane",
                            book.getAuthor(),
                            book.getYear());
                            */
                    UpdateBookResponse response = catalog.updateBook(command);
                    System.out.println( "Updeting book result : " + response.isSuccess());
                });
    }

}
