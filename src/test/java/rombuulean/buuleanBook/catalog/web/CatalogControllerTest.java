package rombuulean.buuleanBook.catalog.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import rombuulean.buuleanBook.catalog.application.port.CatalogUseCase;
import rombuulean.buuleanBook.catalog.domain.Book;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {CatalogController.class})
public class CatalogControllerTest {

    @MockBean
    CatalogUseCase catalog;

    @Autowired
    CatalogController catalogController;

    @Test
    public void shouldGetAllBooks() {

        // give
        Book effective = new Book("Effective Java", 2007, new BigDecimal("99.89"), 50L);
        Book concurency = new Book("Java Concurrency", 2002, new BigDecimal("179.80"), 50L);

        Mockito.when(catalog.findAll()).thenReturn( List.of(effective,concurency ));
        // when
        List<Book> all = catalogController.getAll(Optional.empty(), Optional.empty());
        // then
        assertEquals(2, all.size());

    }


}
