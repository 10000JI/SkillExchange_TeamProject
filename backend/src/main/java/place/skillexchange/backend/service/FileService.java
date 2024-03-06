package place.skillexchange.backend.service;

import org.springframework.web.multipart.MultipartFile;
import place.skillexchange.backend.dto.NoticeDto;
import place.skillexchange.backend.entity.File;
import place.skillexchange.backend.entity.Notice;
import place.skillexchange.backend.entity.User;

import java.io.IOException;
import java.util.List;

public interface FileService {
    public File uploadFilePR(MultipartFile multipartFile, User user) throws IOException;

    public List<File> registerNoticeImg(List<MultipartFile> multipartFiles, Notice notice) throws IOException;

    public List<File> updateNoticeImg(List<String> imgUrls, List<MultipartFile> multipartFiles, Notice notice) throws IOException;
}
