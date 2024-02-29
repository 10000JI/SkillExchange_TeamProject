package place.skillexchange.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import place.skillexchange.backend.dto.NoticeDto;
import place.skillexchange.backend.entity.Notice;
import place.skillexchange.backend.service.NoticeServiceImpl;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/notices/")
public class NoticeController {

    private final NoticeServiceImpl noticeService;

    /**
     * 공지사항 등록
     */
    @PostMapping("/register")
    public NoticeDto.RegisterResponse register(@Validated @RequestPart("noticeDto") NoticeDto.RegisterRequest dto, @RequestPart(value = "imgFiles", required = false) List<MultipartFile> multipartFiles) throws IOException {
        return noticeService.register(dto, multipartFiles);
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
    public NoticeDto.UpdateResponse update(@Validated @RequestPart("noticeDto") NoticeDto.RegisterRequest dto, @RequestPart(value = "imgFiles", required = false) List<MultipartFile> multipartFiles, @PathVariable Long noticeId) throws IOException {
        return noticeService.update(dto, multipartFiles, noticeId);
    }

    /**
     * 공지사항 삭제
     */
    @DeleteMapping("/{noticeId}")
    public NoticeDto.ResponseBasic delete(@PathVariable Long noticeId) {
        return noticeService.delete(noticeId);
    }

    /**
     * 공지사랑 목록
     */
}