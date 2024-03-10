package place.skillexchange.backend.service;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import place.skillexchange.backend.dto.TalentDto;

import java.io.IOException;
import java.util.List;

public interface TalentService {

    public TalentDto.RegisterResponse register(TalentDto.RegisterRequest dto, List<MultipartFile> multipartFiles) throws IOException;

    public TalentDto.writerInfoResponse writerInfo(Long writerId);

    public TalentDto.ReadResponse read(Long writerId);

    public TalentDto.UpdateResponse update(TalentDto.UpdateRequest dto, List<MultipartFile> multipartFiles, Long talentId) throws IOException;

    public TalentDto.ResponseBasic delete(Long talentId);
}
