package rombuulean.buuleanBook.uploads.db;

import org.springframework.data.jpa.repository.JpaRepository;
import rombuulean.buuleanBook.uploads.domain.Upload;

public interface UploadJpaRepository extends JpaRepository<Upload, Long> {




}
