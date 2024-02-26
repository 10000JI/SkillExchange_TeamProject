package place.skillexchange.backend.file;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class UploadFile {
    private String uploadFileName;
    private String storeFileName;
    private String generateHash;

    public UploadFile(String uploadFileName, String storeFileName, String generateHash) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
        this.generateHash = generateHash;
    }
}
