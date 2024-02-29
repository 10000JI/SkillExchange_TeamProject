package place.skillexchange.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import place.skillexchange.backend.dto.NoticeDto;
import place.skillexchange.backend.entity.File;
import place.skillexchange.backend.entity.Notice;
import place.skillexchange.backend.entity.User;
import place.skillexchange.backend.exception.NoticeNotFoundException;
import place.skillexchange.backend.exception.UserNotFoundException;
import place.skillexchange.backend.repository.NoticeRepository;
import place.skillexchange.backend.repository.UserRepository;
import place.skillexchange.backend.util.SecurityUtil;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService{

    private final SecurityUtil securityUtil;
    private final NoticeRepository noticeRepository;
    private final UserRepository userRepository;
    private final FileServiceImpl fileService;

    @Override
    @Transactional
    public NoticeDto.RegisterResponse register(NoticeDto.RegisterRequest dto, List<MultipartFile> multipartFiles) throws IOException {
        String id = securityUtil.getCurrentMemberUsername();
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("해당 유저를 찾지 못했습니다 : " + id));
        if (!Objects.equals(id, dto.getWriter())) {
            throw new UserNotFoundException("로그인한 회원 정보와 글쓴이가 다릅니다.");
        }
        Notice notice = noticeRepository.save(dto.toEntity(user));

        List<File> files = null;
        if (multipartFiles != null) {
            files = fileService.registerNoticeImg(multipartFiles,notice);
        }

        return new NoticeDto.RegisterResponse(user, files , notice,201,"공지가 등록되었습니다.");
    }

    @Override
    public NoticeDto.ReadResponse read(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(() -> new NoticeNotFoundException("존재하지 않는 게시물 번호입니다: " + noticeId));
        return new NoticeDto.ReadResponse(notice,200, "조회하는데 성공하였습니다.");
    }

    @Override
    @Transactional
    public NoticeDto.UpdateResponse update(NoticeDto.RegisterRequest dto, List<MultipartFile> multipartFiles, Long noticeId) throws IOException {
        String id = securityUtil.getCurrentMemberUsername();
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("해당 유저를 찾지 못했습니다 : " + id));
        if (!Objects.equals(id, dto.getWriter())) {
            throw new UserNotFoundException("로그인한 회원 정보와 글쓴이가 다릅니다.");
        }
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(() -> new NoticeNotFoundException("존재하지 않는 게시물 번호입니다: " + noticeId));
        notice.changeNotice(dto);

        List<File> files = fileService.updateNoticeImg(multipartFiles, notice);

        return new NoticeDto.UpdateResponse(user, files , notice,200,"공지가 수정되었습니다.");
    }

    @Override
    @Transactional
    //Notice와 File은 양방향 매핑으로 Notice가 삭제되면 File도 삭제되도록 Cascade 설정을 했기 때문에 @Transactional이 필요
    public NoticeDto.ResponseBasic delete(Long noticeId) {
        Optional<Notice> deletedNotice = noticeRepository.findById(noticeId);
        if (deletedNotice.isPresent()) {
            noticeRepository.deleteById(noticeId);
            return new NoticeDto.ResponseBasic(200, "공지사항이 성공적으로 삭제되었습니다.");
        } else {
            throw new NoticeNotFoundException("존재하지 않는 게시물 번호입니다: " + noticeId);
        }
    }
}
