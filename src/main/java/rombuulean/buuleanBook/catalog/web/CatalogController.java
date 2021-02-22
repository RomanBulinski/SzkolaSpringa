package rombuulean.buuleanBook.catalog.web;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rombuulean.buuleanBook.catalog.application.port.CatalogUseCase;
import rombuulean.buuleanBook.catalog.domain.Book;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequestMapping("/catalog")
@RestController
@AllArgsConstructor
public class CatalogController {

    private final CatalogUseCase catalog;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Book> getAll(
            @RequestParam Optional<String> title,
            @RequestParam Optional<String> author,
            @RequestParam(defaultValue = "10") int limit)
    {

        if(title.isPresent() && author.isPresent()){
            return catalog.findByTitleAndAuthor( title.get(), author.get() );
        } else if(title.isPresent()){
            return catalog.findByTitle( title.get() );
        }else if(author.isPresent()){
            return catalog.findByAuthor( author.get() );
        }else{
            return catalog.findAll().stream().limit(limit).collect(Collectors.toList());
        }
    }

//    @GetMapping(params = {"title"})
//    @ResponseStatus(HttpStatus.OK)
//    public List<Book> getAllFiltered(
//            @RequestParam Optional<String> title,
//            @RequestParam Optional<String> author
//    ) {
//        return  null;
//        return catalog.findByTitle(title);
//    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return catalog
                .findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
