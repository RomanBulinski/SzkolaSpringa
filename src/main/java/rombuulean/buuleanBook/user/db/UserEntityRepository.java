package rombuulean.buuleanBook.user.db;

import org.springframework.data.jpa.repository.JpaRepository;
import rombuulean.buuleanBook.user.domain.UserEntity;

import java.util.Optional;

public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsernameIgnoreCase(String name);

}
