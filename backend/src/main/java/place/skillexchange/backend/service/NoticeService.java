package place.skillexchange.backend.service;

import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import place.skillexchange.backend.dto.NoticeDto;

import java.io.IOException;
import java.util.List;

public interface NoticeService {

    public NoticeDto.RegisterResponse register(NoticeDto.RegisterRequest dto, List<MultipartFile> multipartFiles) throws IOException;

    public NoticeDto.ReadResponse read(Long noticeId);

    public NoticeDto.UpdateResponse update(NoticeDto.RegisterRequest dto, List<MultipartFile> multipartFile, Long noticeId) throws IOException;

    public NoticeDto.ResponseBasic delete(Long noticeId);
}
