package place.skillexchange.backend.file;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class UploadFile {
    private String uploadFileName;
//    private String storeFileName;
    private String fileUrl;

    public UploadFile(String uploadFileName, String fileUrl) {
        this.uploadFileName = uploadFileName;
        this.fileUrl = fileUrl;
    }
}