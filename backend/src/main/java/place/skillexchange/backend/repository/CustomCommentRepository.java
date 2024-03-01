package place.skillexchange.backend.repository;

import place.skillexchange.backend.entity.Comment;

import java.util.List;

public interface CustomCommentRepository {

    //findCommentsByTicketIdWithParentOrderByParentIdAscNullsFirstCreatedAtAsc
    List<Comment> findCommentByNoticeId(Long noticeId);
}
