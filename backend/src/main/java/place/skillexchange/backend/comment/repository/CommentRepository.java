package place.skillexchange.backend.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import place.skillexchange.backend.comment.entity.Comment;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long>,  CustomCommentRepository {

    @Query("select c from Comment c left join fetch c.notice where c.id = :id")
    Optional<Comment> findCommentByIdWithParent(@Param("id") Long id);

}
