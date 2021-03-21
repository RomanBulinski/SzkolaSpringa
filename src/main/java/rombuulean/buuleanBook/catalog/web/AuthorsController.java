package rombuulean.buuleanBook.catalog.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import rombuulean.buuleanBook.catalog.application.port.AuthorsUseCase;
import rombuulean.buuleanBook.catalog.application.port.AuthorsUseCase.CreateAuthorCommand;
import rombuulean.buuleanBook.catalog.domain.Author;
import rombuulean.buuleanBook.catalog.domain.Book;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static rombuulean.buuleanBook.catalog.application.port.AuthorsUseCase.UpdateAuthorCommand;
import static rombuulean.buuleanBook.catalog.application.port.AuthorsUseCase.UpdateAuthorResponse;

@RestController
@AllArgsConstructor
@RequestMapping("/authors")
public class AuthorsController {

    private final AuthorsUseCase authors;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Author> findAll(
            @RequestParam Optional<String> firstName,
            @RequestParam Optional<String> lastName,
            @RequestParam Optional<String> bookTitle
    ) {
        if (firstName.isPresent() && lastName.isPresent()) {
            return authors.findByFirstAndLastName(firstName.get(), lastName.get());
        } else if (firstName.isPresent()) {
            return authors.findByFirstName(firstName.get());
        } else if (lastName.isPresent()) {
            return authors.findByLastName(lastName.get());
        } else if (bookTitle.isPresent()) {
            return authors.findByBookTitle(bookTitle.get());
        } else {
            return authors.findAll();
        }
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getById(@PathVariable Long id) {
        if (id.equals(42L)) {
            throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT, "I am a teapot");
        }
        return authors
                .findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //TODO find out what is problem with removed author
//    @DeleteMapping("/{id}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void deleteById(@PathVariable Long id) {
//        authors.removeById(id);
//    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> addAuthor(@Valid @RequestBody RestAuthorCommand restAuthorCommand) {
        Author author = authors.addAuthor(restAuthorCommand.toCreateCommand());
        return ResponseEntity.created(createdAuthorUri(author)).build();
    }

    private URI createdAuthorUri(Author author) {
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/" + author.getId().toString()).build().toUri();
        return uri;
    }


    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateAuthor(@PathVariable Long id, @RequestBody RestAuthorCommand restAuthorCommand) {
        UpdateAuthorResponse response = authors.updateAuthor(restAuthorCommand.toUpdateAuthorCommand(id));
        if (!response.isSuccess()) {
            String message = String.join(", ", response.getErrors());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }
    }

    @Data
    private static class RestAuthorCommand {

        @NotEmpty
        @NotNull(message = "Please provide a firstName")
        private String firstName;
        @NotEmpty
        @NotNull(message = "Please provide a lastName")
        private String lastName;
        @NotEmpty
        private Set<Book> books;

        CreateAuthorCommand toCreateCommand() {
            return new CreateAuthorCommand(firstName, lastName);
        }

        UpdateAuthorCommand toUpdateAuthorCommand(Long id) {
            return new UpdateAuthorCommand(id, firstName, lastName, books);
        }

    }

}
