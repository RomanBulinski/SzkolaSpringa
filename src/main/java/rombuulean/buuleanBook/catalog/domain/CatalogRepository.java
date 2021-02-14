package rombuulean.buuleanBook.catalog.domain;

import java.util.List;

public interface CatalogRepository {

    List<Book> findAll();

    void save(Book book);

}
