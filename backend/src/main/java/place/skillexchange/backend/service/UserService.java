package place.skillexchange.backend.service;

import place.skillexchange.backend.dto.UserDto;
import place.skillexchange.backend.entity.User;
import place.skillexchange.backend.file.UploadFile;

import java.io.IOException;

public interface UserService {

    public UserDto.ProfileResponse profileUpdate(UserDto.ProfileRequest dto, UploadFile uploadFile) throws IOException;
}
