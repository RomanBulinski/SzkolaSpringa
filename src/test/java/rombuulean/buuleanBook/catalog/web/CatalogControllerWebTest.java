package rombuulean.buuleanBook.catalog.web;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import rombuulean.buuleanBook.catalog.application.port.CatalogUseCase;
import rombuulean.buuleanBook.catalog.domain.Book;
import rombuulean.buuleanBook.user.db.UserEntityRepository;

import java.math.BigDecimal;
import java.util.List;


import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest({CatalogController.class})
@ActiveProfiles("test")
@WithMockUser
public class CatalogControllerWebTest {

    @MockBean
    CatalogUseCase catalog;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void shouldGetAllBooks() throws Exception {

        // give
        Book effective = new Book("Effective Java", 2007, new BigDecimal("99.89"), 50L);
        Book concurency = new Book("Java Concurrency", 2002, new BigDecimal("179.80"), 50L);
        Mockito.when(catalog.findAll()).thenReturn( List.of(effective,concurency ));

        // expect
        mockMvc.perform(MockMvcRequestBuilders.get("/catalog"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

}
