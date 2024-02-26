package place.skillexchange.backend.dto;

import jakarta.persistence.Lob;
import lombok.Getter;
import place.skillexchange.backend.entity.File;
import place.skillexchange.backend.entity.User;
import place.skillexchange.backend.file.UploadFile;

public class FileDto {

    /**
     * 프로필 이미지 추가시 사용할 Dto -> Entity
     */
    public static class ImageDto{
        private String uploadFileName;
//        private String storeFileName;

        private String fileUrl;

        /* Dto -> Entity */
        public File toEntity(UploadFile uploadFile, User user) {
            File file = File.builder()
                    .oriName(uploadFile.getUploadFileName())
                    .user(user)
                    .fileUrl(uploadFile.getFileUrl())
                    .build();
            return file;
        }
    }

    /**
     * 프로필 이미지 수정 시 응답 Dto
     */
    @Getter
    public static class ProfileResponse {
        private String oriName;
//        private String fileName;
        private String fileUrl;
        private int returnCode;
        private String returnMessage;

        /* Entity -> Dto */
        public ProfileResponse(File file, int returnCode, String returnMessage) {
            if (file != null) {
                this.oriName = file.getOriName();
                this.fileUrl = file.getFileUrl();
            } else {
                this.oriName = null;
                this.fileUrl = null;
            }
            this.returnCode = returnCode;
            this.returnMessage = returnMessage;
        }
    }
}
