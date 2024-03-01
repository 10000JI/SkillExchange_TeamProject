package place.skillexchange.backend.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import place.skillexchange.backend.entity.Comment;

import java.util.List;

import static place.skillexchange.backend.entity.QComment.comment;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CustomCommentRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Comment> findCommentByNoticeId(Long noticeId) {
        return queryFactory.selectFrom(comment)
                .leftJoin(comment.parent)
                .fetchJoin()
                .where(comment.notice.id.eq(noticeId))
                .orderBy(
                        comment.parent.id.asc().nullsFirst(),
                        comment.regDate.asc()
                ).fetch();
    }
}
