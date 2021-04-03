package rombuulean.buuleanBook.catalog.web;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import rombuulean.buuleanBook.catalog.application.port.CatalogUseCase;
import rombuulean.buuleanBook.catalog.domain.Book;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CatalogControllerApiTest {

    @LocalServerPort
    private int port;

    @MockBean
    CatalogUseCase catalogUseCase;

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    public void getAllBooks(){

        // give
        Book effective = new Book("Effective Java", 2007, new BigDecimal("99.89"), 50L);
        Book concurency = new Book("Java Concurrency", 2002, new BigDecimal("179.80"), 50L);
        Mockito.when(catalogUseCase.findAll()).thenReturn( List.of(effective,concurency ));
        ParameterizedTypeReference<List<Book>> type = new ParameterizedTypeReference<>() {};
        // when

        String url = "http://localhost:" + port + "/catalog";
//        ResponseEntity<List> response = restTemplate.getForEntity(url, List.class);
        RequestEntity<Void> request = RequestEntity.get(URI.create(url)).build();
        ResponseEntity<List<Book>> response = restTemplate.exchange(request, type);

        // then
        assertEquals(2, response.getBody().size());

    }

}
