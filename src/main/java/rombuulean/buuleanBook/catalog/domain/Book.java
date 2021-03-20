package rombuulean.buuleanBook.catalog.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import rombuulean.buuleanBook.jpa.BaseEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@RequiredArgsConstructor
@ToString(exclude = "authors")
@Entity
//@EqualsAndHashCode(callSuper = true)
//@EntityListeners(AuditingEntityListener.class)
public class Book extends BaseEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Long id;
//    private String uuid = UUID.randomUUID().toString();
    private String title;
    private Integer year;
    private BigDecimal price;
    private Long coverId;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @ManyToMany( cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable
    @JsonIgnoreProperties("books")
    private Set<Author> authors = new HashSet<>();

    public Book(String title, Integer year,BigDecimal price ) {
        this.title = title;
        this.year = year;
        this.price = price;
    }

    public void addAuthor(Author author){
        authors.add(author);
        author.getBooks().add(this);
    }

    public void deleteAuthor(Author author){
        authors.remove(author);
        author.getBooks().remove(this);
    }

    public void deleteAuthors(){
        Book sefl = this;
        authors.forEach(author -> author.getBooks().remove(sefl) );
        authors.clear();
    }




}
