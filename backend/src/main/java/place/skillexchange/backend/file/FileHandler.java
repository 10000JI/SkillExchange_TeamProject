package place.skillexchange.backend.file;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import place.skillexchange.backend.dto.FileDto;
import place.skillexchange.backend.entity.File;
import place.skillexchange.backend.entity.User;
import place.skillexchange.backend.repository.FileRepository;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class FileHandler {

    private final FileRepository fileRepository;
    private final S3Uploader s3Uploader;

    /**
     * 단일 파일 업로드
     */
    @Transactional
    public File uploadFile(MultipartFile multipartFile, User user) throws IOException {

        File file = null;
        if (!multipartFile.isEmpty()) {
            UploadFile images = s3Uploader.upload(multipartFile, "images");
            file = fileRepository.findByUser(user)
                    .map(existingFile -> {
                        existingFile.changeProfileImg(images);
                        return existingFile;
                    })
                    .orElseGet(() -> fileRepository.save(new FileDto.ImageDto().toEntity(images, user)));
        }
        else {
            // 이미지 파일이 전달되지 않은 경우 해당 유저의 파일 정보를 삭제
            file = user.getFile();
            if (file != null) {
                fileRepository.delete(file);
            }
        }

        return file;
    }
}
