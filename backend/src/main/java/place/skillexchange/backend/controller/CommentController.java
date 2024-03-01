package place.skillexchange.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import place.skillexchange.backend.dto.CommentDto;
import place.skillexchange.backend.service.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/comment/")
public class CommentController {

    private final CommentService commentService;

    @GetMapping(value = "/{noticeId}")
    public List<CommentDto.ViewResponse> findAllCommentsByNoticeId(@PathVariable("noticeId") Long noticeId) {
        return commentService.findCommentsByNoticeId(noticeId);
    }

    @PostMapping(value="/register")
    public CommentDto.RegisterResponse createComment(@Validated @RequestBody CommentDto.RegisterRequest dto) {
        return commentService.createComment(dto);
    }
}
