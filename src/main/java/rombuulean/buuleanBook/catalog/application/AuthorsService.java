package rombuulean.buuleanBook.catalog.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import rombuulean.buuleanBook.catalog.application.port.AuthorsUseCase;
import rombuulean.buuleanBook.catalog.db.AuthorJpaRepository;
import rombuulean.buuleanBook.catalog.domain.Author;

import java.util.List;

@Service
@AllArgsConstructor
public class AuthorsService implements AuthorsUseCase {
    private final AuthorJpaRepository authorJpaRepository;

    @Override
    public List<Author> findAll() {
        return authorJpaRepository.findAll();
    }
}
