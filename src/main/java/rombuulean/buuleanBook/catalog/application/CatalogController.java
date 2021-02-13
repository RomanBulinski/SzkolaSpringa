package rombuulean.buuleanBook.catalog.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import rombuulean.buuleanBook.catalog.domain.Book;
import rombuulean.buuleanBook.catalog.domain.CatalogService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CatalogController {

    private final CatalogService service;

    public List<Book> findByTitle(String title){
        return service.findByTitle(title);
    }

    public List<Book> findByAuthor(String name){
        return service.findByAuthor(name);
    }

}
