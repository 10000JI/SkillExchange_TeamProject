package place.skillexchange.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import place.skillexchange.backend.dto.NoticeDto;
import place.skillexchange.backend.entity.File;
import place.skillexchange.backend.entity.Notice;
import place.skillexchange.backend.entity.User;
import place.skillexchange.backend.exception.BoardNotFoundException;
import place.skillexchange.backend.exception.UserNotFoundException;
import place.skillexchange.backend.repository.NoticeRepository;
import place.skillexchange.backend.repository.NoticeRepositoryImpl;
import place.skillexchange.backend.repository.UserRepository;
import place.skillexchange.backend.util.SecurityUtil;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService{

    private final SecurityUtil securityUtil;
    private final NoticeRepository noticeRepository;
    private final NoticeRepositoryImpl noticeRepositoryImpl;
    private final UserRepository userRepository;
    private final FileServiceImpl fileService;

    /**
     * 공지사항 등록
     */
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
        System.out.println("MultiPartFiles:  "+multipartFiles);
        if (multipartFiles != null) {
            files = fileService.registerNoticeImg(multipartFiles,notice);
        }

        return new NoticeDto.RegisterResponse(user, files , notice,201,"공지가 등록되었습니다.");
    }

    /**
     * 공지사항 조회
     */
    // 조회수 업데이트를 위한 별도의 메서드 예시
    @Transactional
    public void increaseHit(Long noticeId) {
        noticeRepository.updateHit(noticeId);
    }

    // 조회 메서드 내에서 조회수 업데이트 호출 예시
    @Override
    @Transactional(readOnly = true)
    public NoticeDto.ReadResponse read(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new BoardNotFoundException("존재하지 않는 게시물 번호입니다: " + noticeId));
        // 별도의 트랜잭션으로 처리하기 위해 분리된 메서드 호출
        increaseHit(noticeId);
        return new NoticeDto.ReadResponse(notice);
    }


    /**
     * 공지사항 업데이트
     */
    @Override
    @Transactional
    public NoticeDto.UpdateResponse update(NoticeDto.UpdateRequest dto, List<MultipartFile> multipartFiles, Long noticeId) throws IOException {
        String id = securityUtil.getCurrentMemberUsername();
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("해당 유저를 찾지 못했습니다 : " + id));
        if (!Objects.equals(id, dto.getWriter())) {
            throw new UserNotFoundException("로그인한 회원 정보와 글쓴이가 다릅니다.");
        }
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(() -> new BoardNotFoundException("존재하지 않는 게시물 번호입니다: " + noticeId));
        notice.changeNotice(dto);

        List<File> files = fileService.updateNoticeImg(dto.getImgUrl(), multipartFiles, notice);

        return new NoticeDto.UpdateResponse(user, files , notice,200,"공지가 수정되었습니다.");
    }

    /**
     * 공지사항 삭제
     */
    @Override
    @Transactional
    //Notice와 File은 양방향 매핑으로 Notice가 삭제되면 File도 삭제되도록 Cascade 설정을 했기 때문에 @Transactional이 필요
    public NoticeDto.ResponseBasic delete(Long noticeId) throws MalformedURLException {
        Optional<Notice> deletedNotice = noticeRepository.findById(noticeId);
        if (deletedNotice.isPresent()) {
            noticeRepository.deleteById(noticeId);
//            fileService.deleteNoticeImg(deletedNotice.get());
            return new NoticeDto.ResponseBasic(200, "공지사항이 성공적으로 삭제되었습니다.");
        } else {
            throw new BoardNotFoundException("존재하지 않는 게시물 번호입니다: " + noticeId);
        }
    }

    /**
     * 공지사항 목록
     */
    @Override
    public Page<NoticeDto.ListResponse> getNotices(int limit, int skip, String keyword) {
        Pageable pageable = PageRequest.of(skip, limit);
        return noticeRepositoryImpl.findNoticesWithPagingAndKeyword(keyword, pageable);
    }
}
