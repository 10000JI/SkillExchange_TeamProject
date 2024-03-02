package place.skillexchange.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    /**
     * 공지사항 게시물 번호의 댓글 조회
     */
    @GetMapping(value = "/{noticeId}")
    public List<CommentDto.ViewResponse> findAllCommentsByNoticeId(@PathVariable("noticeId") Long noticeId) {
        return commentService.findCommentsByNoticeId(noticeId);
    }

    /**
     * 공지사항 댓글 등록
     */
    @PostMapping(value="/register")
    public ResponseEntity<CommentDto.RegisterResponse> createComment(@Validated @RequestBody CommentDto.RegisterRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.createComment(dto));
    }

    /**
     * 공지사항 댓글 삭제
     */
    @DeleteMapping(value = "/{commentId}")
    public CommentDto.ResponseBasic deleteComment(@PathVariable("commentId") Long commentId) {
        return commentService.deleteComment(commentId);
    }
}
