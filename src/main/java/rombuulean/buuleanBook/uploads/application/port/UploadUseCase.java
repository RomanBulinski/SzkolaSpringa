package rombuulean.buuleanBook.uploads.application.port;

import lombok.AllArgsConstructor;
import lombok.Value;
import rombuulean.buuleanBook.uploads.domain.Upload;

public interface UploadUseCase {

    Upload save(SaveUploadCommand command);

    @Value
    @AllArgsConstructor
    class SaveUploadCommand{
        String filename;
        byte[] file;
        String contentType;
    }

}
