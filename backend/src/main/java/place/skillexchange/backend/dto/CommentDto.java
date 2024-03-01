package place.skillexchange.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import place.skillexchange.backend.entity.Comment;
import place.skillexchange.backend.entity.DeleteStatus;
import place.skillexchange.backend.entity.Notice;
import place.skillexchange.backend.entity.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CommentDto {
    //implements Serializable
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ViewResponse {
        private Long id;
        private String content;
        private String userId;
        private List<ViewResponse> children = new ArrayList<>();

        public ViewResponse(Long id, String content, String userId) {
            this.id = id;
            this.content = content;
            this.userId = userId;
        }

        public static ViewResponse entityToDto(Comment comment) {
            return comment.getIsDeleted() == DeleteStatus.Y ?
                    new ViewResponse(comment.getId(), "삭제된 댓글입니다.", null) :
                    new ViewResponse(comment.getId(), comment.getContent(), comment.getWriter().getId());
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RegisterRequest {

        private Long noticeId;

        private Long parentId;

        @NotBlank(message = "작성자: 필수 정보입니다.")
        private String writer;

        @NotBlank(message = "내용: 필수 정보입니다.")
        private String content;

        /* Dto -> Entity */
        public Comment toEntity(User user, Notice notice, Comment parent) {
            Comment comment = Comment.builder()
                    .writer(user)
                    .content(content)
                    .notice(notice)
                    .isDeleted(DeleteStatus.N)
                    .parent(parent)
                    .build();
            return comment;
        }
    }

    @Getter
    public static class RegisterResponse {
        private Long id;
        private String writer;
        private String content;
        private LocalDateTime regDate;
        private int returnCode;
        private String returnMessage;

        /* Dto -> Entity */
        public RegisterResponse(Comment comment, int returnCode, String returnMessage) {
            this.id = comment.getId();
            this.writer = comment.getWriter().getId();
            this.content = comment.getContent();
            this.regDate = comment.getRegDate();
            this.returnCode = returnCode;
            this.returnMessage = returnMessage;
        }
    }
}
