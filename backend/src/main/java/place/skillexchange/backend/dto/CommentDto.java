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
    @Getter
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CommentViewResponse {
        private Long id;
        private String content;
        private String userId;
        private String imgUrl;
        private LocalDateTime regDate;
        private List<CommentViewResponse> children = new ArrayList<>();

        public CommentViewResponse(Long id, String content, String userId, String imgUrl, LocalDateTime regDate) {
            this.id = id;
            this.content = content;
            this.userId = userId;
            this.imgUrl = imgUrl;
            this.regDate = regDate;
        }

        //DeleteStatus(삭제된 상태)가 Y(맞다면)라면 new ViewResponse(comment.getId(), "삭제된 댓글입니다.", null)
        //아니라면(N이라면) new ViewResponse(comment.getId(), comment.getContent(), comment.getWriter().getId())
        public static CommentViewResponse entityToDto(Comment comment) {
            String imgUrl = comment.getWriter() != null && comment.getWriter().getFile() != null ? comment.getWriter().getFile().getFileUrl() : null;
            return comment.getIsDeleted() == DeleteStatus.Y ?
                    new CommentViewResponse(comment.getId(), "삭제된 댓글입니다.", null, imgUrl, comment.getRegDate()) :
                    new CommentViewResponse(comment.getId(), comment.getContent(), comment.getWriter().getId(), imgUrl, comment.getRegDate());
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CommentRegisterRequest {

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
    public static class CommentRegisterResponse {
        private Long id;
        private String writer;
        private String content;
        private LocalDateTime regDate;
        private int returnCode;
        private String returnMessage;

        /* Dto -> Entity */
        public CommentRegisterResponse(Comment comment, int returnCode, String returnMessage) {
            this.id = comment.getId();
            this.writer = comment.getWriter().getId();
            this.content = comment.getContent();
            this.regDate = comment.getRegDate();
            this.returnCode = returnCode;
            this.returnMessage = returnMessage;
        }
    }

    /**
     * 응답코드, 응답메세지
     */
    @Getter
    @AllArgsConstructor
    public static class ResponseBasic {
        private int returnCode;
        private String returnMessage;
    }
}
