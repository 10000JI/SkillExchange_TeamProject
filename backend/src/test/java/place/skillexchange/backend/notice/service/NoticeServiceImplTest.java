package place.skillexchange.backend.notice.service;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Not;
import org.checkerframework.checker.units.qual.N;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.multipart.MultipartFile;
import place.skillexchange.backend.exception.user.WriterAndLoggedInUserMismatchExceptionAll;
import place.skillexchange.backend.file.entity.File;
import place.skillexchange.backend.file.service.FileService;
import place.skillexchange.backend.file.service.FileServiceImpl;
import place.skillexchange.backend.notice.dto.NoticeDto;
import place.skillexchange.backend.notice.entity.Notice;
import place.skillexchange.backend.notice.repository.NoticeRepository;
import place.skillexchange.backend.user.entity.User;
import place.skillexchange.backend.user.repository.UserRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Slf4j
class NoticeServiceImplTest {

    @Mock
    private NoticeRepository noticeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FileServiceImpl fileService;

    @InjectMocks
    private NoticeServiceImpl noticeService;

    @Test
    @DisplayName("공지사항 등록 성공 테스트")
    public void testRegister_Success() throws IOException {
        // Given
        String userId = "testUser";
        String writer = "testUser";
        String title  = "testTitle";
        String content = "testContent";
        String img1 = "img1.jpg";
        String imgUrl1 = "https://.../img1.jpg";
        String img2 = "img2.jpg";
        String imgUrl2 = "https://.../img2.jpg";

        Notice notice = Notice.builder()
                .id(1L) // 임의의 ID 설정
                .writer(User.builder().id(userId).build())
                .title(title)
                .content(content)
                .build();
        List<File> files = new ArrayList<>();
        File file1 = File.builder().oriName(img1).fileUrl(imgUrl1).build();
        File file2 = File.builder().oriName(img2).fileUrl(imgUrl2).build();
        files.add(file1);
        files.add(file2);

        NoticeDto.NoticeRegisterRequest request = NoticeDto.NoticeRegisterRequest.builder()
                .writer(writer)
                .title(title)
                .content(content)
                .build();

        // MultipartFile을 저장할 리스트 생성
        List<MultipartFile> multipartFiles = new ArrayList<>();

        // 첫 번째 이미지 파일을 MockMultipartFile로 생성하여 리스트에 추가
        // MockMultipartFile 생성자 인자 설명:
        // 1. 파일 이름의 시작부터 마지막 점('.')이 나타나는 위치 이전의 문자열을 추출하여 파일 이름으로 사용
        // 2. 파일 이름 전체 (예: img1.jpg)
        // 3. 파일의 콘텐츠 타입 (MIME 타입)
        // 4. 파일의 콘텐츠를 나타내는 바이트 배열 (여기서는 빈 배열을 사용)
        multipartFiles.add(new MockMultipartFile(img1.substring(0, img1.lastIndexOf('.')), img1, "image/jpeg", new byte[0]));
        multipartFiles.add(new MockMultipartFile(img2.substring(0, img2.lastIndexOf('.')), img2, "image/jpeg", new byte[0]));

        // 현재 인증된 사용자 설정
        Authentication authentication = new TestingAuthenticationToken(userId, null, "ROLE_USER");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // userRepository의 동작을 모의화
        when(userRepository.findById(userId)).thenReturn(Optional.of(notice.getWriter()));

        // noticeRepository의 동작을 모의화
        when(noticeRepository.save(any(Notice.class))).thenReturn(notice);

        // fileService의 동작을 모의화
        when(fileService.registerNoticeImg(multipartFiles, notice)).thenReturn(files);


        // When
        NoticeDto.NoticeRegisterResponse response = noticeService.register(request, multipartFiles);

        // Then
        assertNotNull(response);
        assertEquals(userId, response.getWriter());
        for (int i = 0; i < files.size(); i++) {
            assertEquals(files.get(i).getFileUrl(), response.getImgUrl().get(i));
        }
        assertEquals(notice.getContent(), response.getContent());
        assertEquals(201, response.getReturnCode());
        assertEquals("공지가 등록되었습니다.", response.getReturnMessage());

        // userRepository.findById가 올바른 userId로 호출되었는지 확인
        verify(userRepository).findById(userId);
        // noticeRepository.save가 호출되었는지 확인
        verify(noticeRepository).save(any(Notice.class));
        // fileService.registerNoticeImg가 올바른 파라미터와 함께 호출되었는지 확인
        verify(fileService).registerNoticeImg(multipartFiles, notice);
    }

    @Test
    @DisplayName("공지사항 등록 실패: 로그인 한 사용자와 글쓴이와 다른 경우")
    public void testRegister_WriterAndLoggedInUserMismatch() throws IOException {
        // Given
        String userId = "testUser";
        String writer = "anotherUser";
        String title  = "testTitle";
        String content = "testContent";

        // MultipartFile을 저장할 리스트 생성
        List<MultipartFile> multipartFiles = new ArrayList<>();
        NoticeDto.NoticeRegisterRequest request = NoticeDto.NoticeRegisterRequest.builder()
                .writer(writer)
                .title(title)
                .content(content)
                .build();

        // 현재 인증된 사용자 설정
        Authentication authentication = new TestingAuthenticationToken(userId, null, "ROLE_USER");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // userRepository의 동작을 모의화
        when(userRepository.findById(userId)).thenReturn(Optional.of(User.builder().id(userId).build()));

        // When
        Throwable thrown = catchThrowable(() -> noticeService.register(request, multipartFiles));

        // Then
        assertThat(thrown).isInstanceOf(WriterAndLoggedInUserMismatchExceptionAll.class);
    }

    @Test
    @DisplayName("공지사항 조회 테스트")
    public void testRead() {
        // Given
        Long noticeId = 1L;
        List<File> files = new ArrayList<>();
        Notice notice = Notice.builder().id(noticeId).title("testTitle").content("testContent").writer(User.builder().id("testUser").build()).files(files).build();

        when(noticeRepository.findById(noticeId)).thenReturn(Optional.of(notice));

        // When
        NoticeDto.NoticeReadResponse response = noticeService.read(noticeId);

        // Then
        // 해당 공지사항이 반환되었는지 확인
        assertNotNull(response);

        // findById 메서드가 호출되었는지 확인
        verify(noticeRepository).findById(noticeId);
    }

    @Test
    @DisplayName("공지사항 수정 테스트")
    public void testUpdate() throws IOException {
        // Given
        String userId = "testUser";
        String writer = "testUser";
        String title  = "testTitle";
        String content = "testContent";
        String img1 = "img1.jpg";
        String imgUrl1 = "https://.../img1.jpg";
        String img3 = "img3.jpg";
        String imgUrl3 = "https://.../img3.jpg";
        Long noticeId = 1L;

        Notice notice = Notice.builder()
                .id(noticeId)
                .writer(User.builder().id(userId).build())
                .title(title)
                .content(content)
                .build();
        List<File> files = new ArrayList<>();
        files.add(File.builder().oriName(img1).fileUrl(imgUrl1).build());
        files.add(File.builder().oriName(img3).fileUrl(imgUrl3).build());

        //이전에 등록한 이미지 그대로 사용하기 위한 imgUrl들이 담긴 List
        List<String> imgUrl = new ArrayList<>();
        imgUrl.add(img1);
        //수정할 공지사항 게시물 필드
        NoticeDto.NoticeUpdateRequest request = NoticeDto.NoticeUpdateRequest.builder()
                .writer(writer)
                .title(title)
                .content(content)
                .imgUrl(imgUrl)
                .build();
        //새로 요청된 이미지 파일
        List<MultipartFile> multipartFiles = new ArrayList<>();
        multipartFiles.add(new MockMultipartFile(img3.substring(0, img3.lastIndexOf('.')), img3, "image/jpeg", new byte[0]));

        // 현재 인증된 사용자 설정
        Authentication authentication = new TestingAuthenticationToken(userId, null, "ROLE_USER");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // userRepository의 동작을 모의화
        when(userRepository.findById(userId)).thenReturn(Optional.of(notice.getWriter()));
        // noticeRepository 동작을 모의화
        when(noticeRepository.findById(noticeId)).thenReturn(Optional.of(notice));
        // fileService의 동작을 모의화
        when(fileService.updateNoticeImg(imgUrl, multipartFiles, notice)).thenReturn(files);

        // When
        NoticeDto.NoticeUpdateResponse response = noticeService.update(request, multipartFiles, noticeId);


        // Then
        assertNotNull(response);
        assertEquals(userId, response.getWriter());
        for (int i = 0; i < files.size(); i++) {
            assertEquals(files.get(i).getFileUrl(), response.getImgUrl().get(i));
        }
        assertEquals(notice.getContent(), response.getContent());
        assertEquals(200, response.getReturnCode());
        assertEquals("공지가 수정되었습니다.", response.getReturnMessage());

        // userRepository.findById가 올바른 userId로 호출되었는지 확인
        verify(userRepository).findById(userId);
        // fileService.registerNoticeImg가 올바른 파라미터와 함께 호출되었는지 확인
        verify(fileService).updateNoticeImg(imgUrl, multipartFiles, notice);
    }
}