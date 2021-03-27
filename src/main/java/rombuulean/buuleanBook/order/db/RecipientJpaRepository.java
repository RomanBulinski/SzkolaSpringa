package rombuulean.buuleanBook.order.db;

import org.springframework.data.jpa.repository.JpaRepository;
import rombuulean.buuleanBook.order.domain.Recipient;

import java.util.Optional;

public interface RecipientJpaRepository extends JpaRepository<Recipient, Long> {

    Optional<Recipient> findByEmailIgnoreCase(String email);

}
