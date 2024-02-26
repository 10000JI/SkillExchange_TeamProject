package place.skillexchange.backend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import place.skillexchange.backend.dto.FileDto;
import place.skillexchange.backend.dto.UserDto;
import place.skillexchange.backend.service.FileServiceImpl;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/file/")
@Slf4j
public class FileController {

    private final FileServiceImpl fileService;

    @PatchMapping("/profileImage")
    public FileDto.ProfileResponse profileUpdate(@RequestPart(value="imgFile", required = false) MultipartFile multipartFile) throws IOException {
        return fileService.profileUpdate(multipartFile);
    }
}
