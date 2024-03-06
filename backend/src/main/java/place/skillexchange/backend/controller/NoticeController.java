package place.skillexchange.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import place.skillexchange.backend.dto.NoticeDto;
import place.skillexchange.backend.service.NoticeService;
import place.skillexchange.backend.service.NoticeServiceImpl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/notices/")
public class NoticeController {

    private final NoticeService noticeService;

    /**
     * 공지사항 등록
     */
    @PostMapping("/register")
    public ResponseEntity<NoticeDto.RegisterResponse> register(@Validated @RequestPart("noticeDto") NoticeDto.RegisterRequest dto, @RequestPart(value = "files", required = false) List<MultipartFile> multipartFiles) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED).body(noticeService.register(dto, multipartFiles));
    }

    /**
     * 공지사항 조회
     */
    @GetMapping("/{noticeId}")
    public NoticeDto.ReadResponse read(@PathVariable Long noticeId) {
        return noticeService.read(noticeId);
    }

    /**
     * 공지사항 수정
     */
    @PatchMapping("/{noticeId}")
    public NoticeDto.UpdateResponse update(@Validated @RequestPart("noticeDto") NoticeDto.UpdateRequest dto, @RequestPart(value = "files", required = false) List<MultipartFile> multipartFiles, @PathVariable Long noticeId) throws IOException {
        return noticeService.update(dto, multipartFiles, noticeId);
    }

    /**
     * 공지사항 삭제
     */
    @DeleteMapping("/{noticeId}")
    public NoticeDto.ResponseBasic delete(@PathVariable Long noticeId) throws MalformedURLException {
        return noticeService.delete(noticeId);
    }

    /**
     * 공지사랑 목록
     */
    @GetMapping("/list")
    public ResponseEntity<Page<NoticeDto.ListResponse>> getNotices(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int skip,
            @RequestParam(required = false) String keyword) {

        Page<NoticeDto.ListResponse> notices = noticeService.getNotices(limit, skip, keyword);

        return ResponseEntity.ok(notices);
    }
}