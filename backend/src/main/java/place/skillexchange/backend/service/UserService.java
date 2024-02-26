package place.skillexchange.backend.service;

import org.springframework.web.multipart.MultipartFile;
import place.skillexchange.backend.dto.UserDto;
import place.skillexchange.backend.entity.User;
import place.skillexchange.backend.file.UploadFile;

import java.io.IOException;

public interface UserService {

    public UserDto.ProfileResponse profileUpdate(UserDto.ProfileRequest dto) throws IOException;
}
