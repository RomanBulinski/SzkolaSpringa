package rombuulean.buuleanBook.catalog.infrastructure;

import org.springframework.stereotype.Repository;
import rombuulean.buuleanBook.catalog.domain.Book;
import rombuulean.buuleanBook.catalog.domain.CatalogRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
    /*
    @Primary wskazuje który Bean ma pierwszenstwo przy wywolaniu,
    jezeli jest kilka beanów tego samego typu
    */
class SchoolCatalogRepositoryImpl implements CatalogRepository {

    private final Map<Long, Book> storage = new ConcurrentHashMap<>();

    public SchoolCatalogRepositoryImpl() {
        storage.put(1L, new Book(1L,"Pan Tadeusz", "Adam Mickiewicz", 1834));
        storage.put(2L, new Book(2L,"Ogniem i mieczem", "Henryk Sienkiewicz", 1884));
        storage.put(3L, new Book(3L,"Chłopi", "Władysława Reymont", 1834));
        storage.put(5L, new Book(4L,"Pan Wołodyjowski", "Henryk Sienkiewicz", 2000));
    }

    @Override
    public List<Book> findAll() {
        return new ArrayList<>(storage.values());
    }
}
