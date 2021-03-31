package rombuulean.buuleanBook.catalog.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import rombuulean.buuleanBook.catalog.application.port.CatalogUseCase;
import rombuulean.buuleanBook.catalog.db.AuthorJpaRepository;
import rombuulean.buuleanBook.catalog.domain.Author;
import rombuulean.buuleanBook.catalog.domain.Book;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static rombuulean.buuleanBook.catalog.application.port.CatalogUseCase.CreateBookCommand;

@SpringBootTest
@DirtiesContext( classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
class CatalogControllerIT {

    @Autowired
    AuthorJpaRepository authorJpaRepository;
    @Autowired
    CatalogUseCase catalogUseCase;
    @Autowired
    CatalogController catalogController;

    @Test
    public void getAllBooks() {

        //given
        givenEffetctiveJava();
        givenJavaInPractice();
        //when
        List<Book> all = catalogController.getAll(Optional.empty(), Optional.empty());
        //then
        assertEquals(2, all.size());

    }

    @Test
    public void getAllBooksByAuthor() {

        //given
        givenEffetctiveJava();
        givenJavaInPractice();
        //when
        List<Book> all = catalogController.getAll(Optional.empty(), Optional.of("Bloch"));
        //then
        assertEquals(1, all.size());
        assertEquals( "Effective Java",all.get(0).getTitle());

    }

    @Test
    public void getAllBooksByTitle() {

        //given
        givenEffetctiveJava();
        givenJavaInPractice();
        //when
        List<Book> all = catalogController.getAll(Optional.of("Effective Java"), Optional.empty());
        //then
        assertEquals(1, all.size());
        assertEquals( "Effective Java",all.get(0).getTitle());
    }

    private void givenJavaInPractice( ) {
        Author goetz = authorJpaRepository.save(new Author("Brian Goetz"));
        catalogUseCase.addBook(new CreateBookCommand(
                "Java in Practice",
                Set.of(goetz.getId()),
                2002,
                new BigDecimal("109.89"),
                50L
        ));
    }

    private void givenEffetctiveJava() {
        Author bloch = authorJpaRepository.save(new Author("Joshua Bloch"));
        catalogUseCase.addBook(new CreateBookCommand(
                "Effective Java",
                Set.of(bloch.getId()),
                2007,
                new BigDecimal("99.89"),
                50L
        ));
    }

}
