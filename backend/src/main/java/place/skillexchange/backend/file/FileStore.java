package place.skillexchange.backend.file;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import place.skillexchange.backend.repository.FileRepository;
import place.skillexchange.backend.util.ImageUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class FileStore {
    @Value("${file.dir}")
    private String fileDir;

    private final FileRepository fileRepository;

    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    //단일 파일이 아닌, 여러개의 파일을 업로드할 때
    public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles) throws IOException {
        List<UploadFile> storeFileResult = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                storeFileResult.add(storeFile(multipartFile));
            }
        }
        return storeFileResult;
    }

    //단일 파일 업로드
    public UploadFile storeFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename(); //image.png
        String storeFileName = createStoreFileName(originalFilename); //서버에 저장하는 파일명
        String fileHash = ImageUtils.generateHash(multipartFile.getBytes()); // 파일의 해시 생성

        // 이미지가 존재하는지 확인하고, 동일한 해시가 이미 존재하는지 여부를 확인
        boolean isDuplicate = fileRepository.existsByGenerateHash(fileHash);

        // 이미지가 존재하지 않거나 동일한 해시가 없는 경우에만 이미지 저장
        if (!isDuplicate) {
            multipartFile.transferTo(new File(getFullPath(storeFileName)));
        }
        return new UploadFile(originalFilename, storeFileName, fileHash);
    }

    //서버에 저장하는 파일명 (UUID)
    private String createStoreFileName(String originalFilename) {
        String uuid = UUID.randomUUID().toString(); // "qwe-qwe-123-qwe-qweq"
        String ext = extractExt(originalFilename);
        return uuid + "." + ext; // "qwe-qwe-123-qwe-qweq.png"
    }

    //확장자
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}
