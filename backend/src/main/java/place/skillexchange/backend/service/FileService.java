package place.skillexchange.backend.service;

import org.springframework.web.multipart.MultipartFile;
import place.skillexchange.backend.dto.FileDto;

import java.io.IOException;

public interface FileService {
    public FileDto.ProfileResponse profileUpdate(MultipartFile multipartFile) throws IOException;
}
