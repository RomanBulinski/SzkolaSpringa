package rombuulean.buuleanBook.catalog.application;

import org.springframework.stereotype.Controller;
import rombuulean.buuleanBook.catalog.domain.Book;
import rombuulean.buuleanBook.catalog.domain.CatalogService;

import java.util.List;

@Controller
public class CatalogController {

    private final CatalogService service;

    public CatalogController(CatalogService service) {
        this.service = service;
    }

    public List<Book> findByTitle(String title){
        return service.findByTitle(title);
    }


}
