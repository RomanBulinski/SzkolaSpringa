package rombuulean.buuleanBook.catalog.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import rombuulean.buuleanBook.catalog.application.port.CatalogUseCase;
import rombuulean.buuleanBook.catalog.application.port.CatalogUseCase.CreateBookCommand;
import rombuulean.buuleanBook.catalog.domain.Book;

import java.math.BigDecimal;
import java.net.URI;
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
//    public Book addBook(@RequestBody  RestCreateBookCommand command){
    public ResponseEntity<Void> addBook(@RequestBody  RestCreateBookCommand command){
        Book book =catalog.addBook(command.toCommand());
        return ResponseEntity.created(createdBookuri(book)).build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id){
        catalog.removeById(id);
    }

    private URI createdBookuri(Book book) {
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/"+ book.getId().toString()).build().toUri();
        return uri;
    }

    @Data
    private static class RestCreateBookCommand {
        private String title;
        private String author;
        private Integer year;
        private BigDecimal price;

        CreateBookCommand toCommand(){
            return new CreateBookCommand(title, author, year, price);
        }

    }


}
