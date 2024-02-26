package place.skillexchange.backend.dto;

import jakarta.persistence.Lob;
import place.skillexchange.backend.entity.File;
import place.skillexchange.backend.entity.User;
import place.skillexchange.backend.file.UploadFile;

public class FileDto {

    /**
     * 프로필 이미지 추가시 사용할 Dto -> Entity
     */
    public static class ImageDto{
        private String uploadFileName;
        private String storeFileName;

        private String generateHash; // BLOB 바이너리 데이터

        /* Dto -> Entity */
        public File toEntity(UploadFile uploadFile, User user) {
            File file = File.builder()
                    .fileName(uploadFile.getStoreFileName())
                    .oriName(uploadFile.getUploadFileName())
                    .user(user)
                    .generateHash(uploadFile.getGenerateHash())
                    .build();
            return file;
        }
    }
}
