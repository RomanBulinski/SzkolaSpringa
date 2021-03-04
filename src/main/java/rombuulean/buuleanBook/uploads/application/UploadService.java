package rombuulean.buuleanBook.uploads.application;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import rombuulean.buuleanBook.uploads.application.port.UploadUseCase;
import rombuulean.buuleanBook.uploads.db.UploadJpaRepository;
import rombuulean.buuleanBook.uploads.domain.Upload;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@AllArgsConstructor
public class UploadService implements UploadUseCase {
    private final UploadJpaRepository uploadJpaRepository;

    @Override
    public Upload save(SaveUploadCommand command) {
        Upload upload = new Upload(
                command.getFilename(),
                command.getContentType(),
                command.getFile()
        );
        uploadJpaRepository.save( upload );
        System.out.println("Upload saved : " + upload.getFilename() + " with id: " + upload.getId());
        return upload;
    }

    @Override
    public Optional<Upload> getById(Long id) {
        return uploadJpaRepository.findById(id);
    }

    @Override
    public void removeById(Long id) {
        uploadJpaRepository.deleteById(id);
    }

}
