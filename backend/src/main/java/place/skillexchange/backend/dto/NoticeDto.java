package place.skillexchange.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import place.skillexchange.backend.entity.File;
import place.skillexchange.backend.entity.Notice;
import place.skillexchange.backend.entity.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NoticeDto {

    /**
     * 게시물 등록 시 요청된 Dto
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RegisterRequest {

        @NotBlank(message = "작성자: 필수 정보입니다.")
        private String writer;

        @NotBlank(message = "제목: 필수 정보입니다.")
        private String title;

        @NotBlank(message = "내용: 필수 정보입니다.")
        private String content;

        /* Dto -> Entity */
        public Notice toEntity(User user) {
            Notice notice = Notice.builder()
                    .writer(user)
                    .title(title)
                    .content(content)
                    .hit(0L)
                    .build();
            return notice;
        }
    }

    /**
     * 게시물 등록 성공시 보낼 Dto
     */
    @Getter
    public static class RegisterResponse {
        private Long id;
        private String writer;
        private String title;
        private String content;
        private LocalDateTime regDate;
        private LocalDateTime modDate;
        private List<String> oriName = new ArrayList<>();
        private List<String> imgUrl = new ArrayList<>();
        private int returnCode;
        private String returnMessage;

        /* Entity -> Dto */
        public RegisterResponse(User user, List<File> files , Notice notice, int returnCode, String returnMessage) {
            this.writer = user.getId();
            this.id = notice.getId();
            this.title = notice.getTitle();
            this.content = notice.getContent();
            this.regDate = notice.getRegDate();
            this.modDate = notice.getModDate();


            if (files != null && !files.isEmpty()) {
                for (File file : files) {
                    this.oriName.add(file.getOriName());
                    this.imgUrl.add(file.getFileUrl());
                }
            } else {
                this.oriName = null;
                this.imgUrl = null;
            }
            this.returnCode = returnCode;
            this.returnMessage = returnMessage;
        }
    }

    /**
     * 게시물 조회 성공시 보낼 Dto
     */
    @Getter
    public static class ReadResponse {
        private Long id;
        private String writer;
        private String avatar;
        private String title;
        private String content;
        private LocalDateTime regDate;
        private LocalDateTime modDate;
        private List<String> imgUrl = new ArrayList<>();
        private Long hit;

        /* Entity -> Dto */
        public ReadResponse(Notice notice) {
            this.writer = notice.getWriter().getId();
            if (notice.getWriter() != null && notice.getWriter().getFile() != null) {
                this.avatar = notice.getWriter().getFile().getFileUrl();
            } else {
                this.avatar = null;
            }
            this.id = notice.getId();
            this.title = notice.getTitle();
            this.content = notice.getContent();
            this.regDate = notice.getRegDate();
            this.modDate = notice.getModDate();
            this.hit = notice.getHit();


            if (!notice.getFiles().isEmpty()) {
                //자바8 람다식 + forEach 메서드
                notice.getFiles().forEach(file -> this.imgUrl.add(file.getFileUrl()));
            } else {
                this.imgUrl = null;
            }
        }
    }

    /**
     * 게시물 수정 시 요청된 Dto
     */
    @Getter
    public static class UpdateRequest {

        @NotBlank(message = "작성자: 필수 정보입니다.")
        private String writer;

        @NotBlank(message = "제목: 필수 정보입니다.")
        private String title;

        @NotBlank(message = "내용: 필수 정보입니다.")
        private String content;

        private List<String> imgUrl = new ArrayList<>();

    }

    /**
     * 게시물 수정 성공시 보낼 Dto
     */
    @Getter
    public static class UpdateResponse {
        private Long id;
        private String writer;
        private String title;
        private String content;
        private LocalDateTime regDate;
        private LocalDateTime modDate;
        private List<String> oriName = new ArrayList<>();
        private List<String> imgUrl = new ArrayList<>();
        private int returnCode;
        private String returnMessage;

        /* Entity -> Dto */
        public UpdateResponse(User user, List<File> files , Notice notice, int returnCode, String returnMessage) {
            this.writer = user.getId();
            this.id = notice.getId();
            this.title = notice.getTitle();
            this.content = notice.getContent();
            this.regDate = notice.getRegDate();
            this.modDate = notice.getModDate();


            if (files != null && !files.isEmpty()) {
                for (File file : files) {
                    this.oriName.add(file.getOriName());
                    this.imgUrl.add(file.getFileUrl());
                }
            } else {
                //이미지 미첨부 시
                this.oriName = null;
                this.imgUrl = null;
            }
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


    /**
     * 게시물 목록 응답 Dto
     */
    @Getter
    public static class ListResponse {
        private Long id;
        private String writer;
        private String title;
        private Long hit;
        private LocalDateTime regDate;

        public ListResponse(Notice notice) {
            this.id = notice.getId();
            this.writer = notice.getWriter().getId();
            this.title = notice.getTitle();
            this.hit = notice.getHit();
            this.regDate = notice.getRegDate();
        }
    }
}
