package rombuulean.buuleanBook.catalog.infrastructure;

import org.springframework.stereotype.Repository;
import rombuulean.buuleanBook.catalog.domain.Book;
import rombuulean.buuleanBook.catalog.domain.CatalogRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
class MemoryCatalogRepository implements CatalogRepository {

    private final Map<Long, Book> storage = new ConcurrentHashMap<>();
    private final AtomicLong ID_NEXT_VALUE = new AtomicLong(0L);

    @Override
    public List<Book> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void save(Book book) {
        if(book.getId() != null){
            storage.put(book.getId(), book);
        }else{
            long nextId = nextId();
            book.setId(nextId);
            storage.put(nextId, book);
        }
    }

    private long nextId() {
        return ID_NEXT_VALUE.getAndIncrement();
    }

    @Override
    public Optional<Book> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }
}
