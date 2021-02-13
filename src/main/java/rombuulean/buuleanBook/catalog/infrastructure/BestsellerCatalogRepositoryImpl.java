package rombuulean.buuleanBook.catalog.infrastructure;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import rombuulean.buuleanBook.catalog.domain.Book;
import rombuulean.buuleanBook.catalog.domain.CatalogRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
class BestsellerCatalogRepositoryImpl implements CatalogRepository {

    private final Map<Long, Book> storage = new ConcurrentHashMap<>();

    public BestsellerCatalogRepositoryImpl() {
        storage.put(1L, new Book(1L,"Via Carpatia", "Ziemowit Szczerek", 1834));
        storage.put(2L, new Book(2L,"Boże igrzysko", "Norman Davis", 1884));
        storage.put(3L, new Book(3L,"Monte Cassino", "Melchior Wańkowicz", 1834));
        storage.put(4L, new Book(4L,"Jadąc do Badadag", "Andrzej Stasiuk", 2000));
    }

    @Override
    public List<Book> findAll() {
        return new ArrayList<>(storage.values());
    }



}
