package place.skillexchange.backend.comment.service;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import place.skillexchange.backend.comment.dto.CommentDto;
import place.skillexchange.backend.comment.entity.Comment;
import place.skillexchange.backend.comment.entity.DeleteStatus;
import place.skillexchange.backend.comment.repository.CommentRepository;
import place.skillexchange.backend.exception.board.CommentNotFoundException;
import place.skillexchange.backend.file.entity.File;
import place.skillexchange.backend.notice.dto.NoticeDto;
import place.skillexchange.backend.notice.entity.Notice;
import place.skillexchange.backend.notice.repository.NoticeRepository;
import place.skillexchange.backend.user.entity.User;
import place.skillexchange.backend.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Slf4j
class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private NoticeRepository noticeRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    @Test
    @DisplayName("공지사항 게시물 번호의 댓글 조회 테스트")
    public void testFindCommentsByNoticeId() {
        // Given
        Long noticeId = 1L;
        Long parentId = 1L;
        Long childId = 2L;
        String userId = "testUser";
        String writer = "testUser";
        String parentContent = "testParentContent";
        String childContent = "testChildContent";

        Notice notice = Notice.builder().id(noticeId).build();
        User user = User.builder().id(writer).build();
        //자식 댓글 Entity 미리 선언
        Comment child = new Comment();
        //부모의 자식 댓글 리스트
        List<Comment> childs = new ArrayList<>();
        childs.add(child);
        //부모 댓글 선언과 동시에 초기화
        Comment parent = Comment.builder().id(parentId).content(parentContent).isDeleted(DeleteStatus.N).writer(user).notice(notice).children(childs).build();
        //자식 댓글 초기화
        child = Comment.builder().id(childId).content(childContent).isDeleted(DeleteStatus.N).writer(user).notice(notice).parent(parent).build();
        // 공지사항에 대한 댓글 목록 생성
        List<Comment> comments = new ArrayList<>();
        comments.add(parent);
        comments.add(child);

        // Mock 설정: noticeRepository.findById() 메서드가 공지사항을 반환하도록 설정
        when(noticeRepository.findById(noticeId)).thenReturn(Optional.of(notice));

        // Mock 설정: commentRepository.findCommentByNoticeId() 메서드가 댓글 목록을 반환하도록 설정
        when(commentRepository.findCommentByNoticeId(noticeId)).thenReturn(comments);

        // When
        List<CommentDto.CommentViewResponse> response = commentService.findCommentsByNoticeId(noticeId);

        // Then
        assertThat(response.get(0).getId()).isEqualTo(parentId);
        assertThat(response.get(0).getContent()).isEqualTo(parentContent);
        assertThat(response.get(0).getUserId()).isEqualTo(writer);
        assertThat(response.get(0).getChildren().get(0).getId()).isEqualTo(childId);
        assertThat(response.get(0).getChildren().get(0).getContent()).isEqualTo(childContent);
    }

    @Test
    @DisplayName("공지사항 게시물 번호의 댓글 등록 테스트")
    public void testCreateComment() {
        // Given
        Long noticeId = 1L;
        Long parentId = 1L;
        Long childId = 2L;
        String userId = "testUser";
        String writer = "testUser";
        String content = "testContent";

        //예상으로 반환되는 객체
        User user = User.builder().id(writer).build();
        Notice notice = Notice.builder().id(noticeId).build();
        Comment parent = Comment.builder().id(parentId).build();
        Comment child = Comment.builder().id(childId).content(content).writer(user).notice(notice).isDeleted(DeleteStatus.N).parent(parent).build();

        CommentDto.CommentRegisterRequest request = CommentDto.CommentRegisterRequest.builder()
                .noticeId(noticeId)
                .parentId(parentId)
                .writer(writer)
                .content(content)
                .build();

        // 현재 인증된 사용자 설정
        Authentication authentication = new TestingAuthenticationToken(userId, null, "ROLE_USER");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // userRepository의 동작을 모의화
        when(userRepository.findById(userId)).thenReturn(Optional.of(User.builder().id(userId).build()));
        //noticeRepository의 동작을 모의화
        when(noticeRepository.findById(noticeId)).thenReturn(Optional.of(notice));
        //commentRepository의 동작을 모의화
        when(commentRepository.findById(parentId)).thenReturn(Optional.of(parent));
        when(commentRepository.save(any(Comment.class))).thenReturn(child);

        // When
        CommentDto.CommentRegisterResponse response = commentService.createComment(request);

        // Then
        assertNotNull(response);
        assertThat(response.getId()).isEqualTo(childId);
        assertThat(response.getWriter()).isEqualTo(writer);
        assertThat(response.getContent()).isEqualTo(content);
        assertThat(response.getReturnCode()).isEqualTo(201);
        assertThat(response.getReturnMessage()).isEqualTo("댓글이 성공적으로 등록되었습니다.");

        // userRepository.findById가 올바른 userId로 호출되었는지 확인
        verify(userRepository).findById(userId);
        verify(noticeRepository).findById(noticeId);
        verify(commentRepository).findById(parentId);
        verify(commentRepository).save(any(Comment.class));
    }

}