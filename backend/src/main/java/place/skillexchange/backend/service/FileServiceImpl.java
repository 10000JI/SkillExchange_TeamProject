package place.skillexchange.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import place.skillexchange.backend.dto.FileDto;
import place.skillexchange.backend.entity.File;
import place.skillexchange.backend.entity.Notice;
import place.skillexchange.backend.entity.User;
import place.skillexchange.backend.exception.NoticeNotFoundException;
import place.skillexchange.backend.file.S3Uploader;
import place.skillexchange.backend.file.UploadFile;
import place.skillexchange.backend.repository.FileRepository;
import place.skillexchange.backend.repository.NoticeRepository;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class FileServiceImpl implements FileService{

    private final FileRepository fileRepository;
    private final S3Uploader s3Uploader;
    private final NoticeRepository noticeRepository;

    /**
     * 단일 파일 업로드, 프로필 이미지
     */
    @Transactional
    @Override
    public File uploadFilePR(MultipartFile multipartFile, User user) throws IOException {

        File file = null;
        if (!multipartFile.isEmpty()) {
            UploadFile image = s3Uploader.upload(multipartFile, "images");
            file = fileRepository.findByUser(user)
                    .map(existingFile -> {
                        //이미 저장되어 있던 이미지 대신에 현재 업로드 하는 이미지로 업데이트
                        existingFile.changeProfileImg(image);
                        return existingFile;
                    })
                    //이미지 업로드가 처음이라면 dto를 entity로 변경한 후 해당 user의 image 저장
                    .orElseGet(() -> fileRepository.save(new FileDto.ProfileDto().toEntity(image, user)));
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

    /**
     * 다중 파일 업로드 ( 공지사항 생성 )
     */
    @Override
    public List<File> registerNoticeImg(List<MultipartFile> multipartFiles, Notice notice) throws IOException {
        List<File> images = new ArrayList<>();
        if (multipartFiles != null) {
            for (MultipartFile multipartFile : multipartFiles) {
                if (!multipartFile.isEmpty()) {
                    UploadFile image = s3Uploader.upload(multipartFile, "images");
                    // 새로운 파일 생성
                    File newFile = fileRepository.save(new FileDto.NoticeDto().toEntity(image, notice));
                    images.add(newFile);
                }
            }
        }
        return images;
    }


    /**
     * 다중 파일 업로드 ( 공지사항 수정 )
     */
    @Override
    public List<File> updateNoticeImg(List<MultipartFile> multipartFiles, Notice notice) throws IOException {
        List<File> updatedImages  = new ArrayList<>();

        //이전에 저장한 이미지가 있다면 삭제
        List<File> files = fileRepository.findAllByNotice(notice);
        if (files != null && !files.isEmpty()) {
            for (File file : files) {
                //db에서 이미지 삭제
                fileRepository.delete(file);

                String fileUrl = file.getFileUrl(); // File 객체에서 URL을 가져옴
                URL url = new URL(fileUrl); // URL 객체 생성
                String filePath = url.getPath(); // URL의 경로 부분을 추출
                // 맨 앞의 '/' 제거
                String cleanedPath = filePath.substring(1);
                //s3에서 이미지 삭제
                s3Uploader.delete(cleanedPath);
            }
        }


        // 수정 시 이미지 첨부를 한 경우
        if (multipartFiles != null && !multipartFiles.isEmpty()) {
            for (MultipartFile multipartFile : multipartFiles) {
                if (!multipartFile.isEmpty()) {
                    UploadFile uploadedImage = s3Uploader.upload(multipartFile, "images");

                    // 새로운 파일 생성
                    File newFile = fileRepository.save(new FileDto.NoticeDto().toEntity(uploadedImage, notice));
                    updatedImages.add(newFile);
                }
            }
            notice.updateModDate();
        }

        return updatedImages;
    }

    /**
     * 다중 이미지 파일 s3에서 삭제 (공지사항 삭제)
     */
    public void deleteNoticeImg(Notice notice) throws MalformedURLException {
        List<File> files = notice.getFiles();// URL 가져오기
        for (File file : files) {
            String fileUrl = file.getFileUrl(); // File 객체에서 URL을 가져옴
            URL url = new URL(fileUrl); // URL 객체 생성
            String filePath = url.getPath(); // URL의 경로 부분을 추출
            // 맨 앞의 '/' 제거
            String cleanedPath = filePath.substring(1);
            log.error("fileName:::: {}",cleanedPath);
            s3Uploader.delete(cleanedPath);
        }
    }
}
