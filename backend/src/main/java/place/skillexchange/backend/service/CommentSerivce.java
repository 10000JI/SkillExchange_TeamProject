package place.skillexchange.backend.service;

import place.skillexchange.backend.dto.CommentDto;

import java.util.List;

public interface CommentSerivce {
    public List<CommentDto.ViewResponse> findCommentsByNoticeId(Long noticeId);

    public CommentDto.RegisterResponse createComment(CommentDto.RegisterRequest dto);

    public CommentDto.ResponseBasic deleteComment(Long commentId);
}
