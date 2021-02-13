package rombuulean.buuleanBook.catalog.domain;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class CatalogService {

    public CatalogService( @Qualifier("schoolCatalogRepositoryImpl") CatalogRepository repository) {
        this.repository = repository;
    }

    private final CatalogRepository repository;

    public List<Book> findByTitle(String title) {
        return repository.findAll()
                .stream()
                .filter(book -> book.getTitle().startsWith(title))
                .collect(Collectors.toList());
    }


}
