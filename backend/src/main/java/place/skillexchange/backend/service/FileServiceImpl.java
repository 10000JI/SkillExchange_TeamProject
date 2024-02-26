package place.skillexchange.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import place.skillexchange.backend.dto.FileDto;
import place.skillexchange.backend.entity.File;
import place.skillexchange.backend.entity.User;
import place.skillexchange.backend.file.S3Uploader;
import place.skillexchange.backend.file.UploadFile;
import place.skillexchange.backend.repository.FileRepository;
import place.skillexchange.backend.repository.UserRepository;
import place.skillexchange.backend.util.SecurityUtil;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final SecurityUtil securityUtil;
    private final UserRepository userRepository;
    private final FileRepository fileRepository;
    private final S3Uploader s3Uploader;

    @Override
    @Transactional
    public FileDto.ProfileResponse profileUpdate(MultipartFile multipartFile) throws IOException {
        String id = securityUtil.getCurrentMemberUsername();
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("해당 유저를 찾지 못했습니다 : " + id));

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

        return new FileDto.ProfileResponse(file, 200, "프로필 사진이 성공적으로 변경되었습니다.");
    }
}
