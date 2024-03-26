package place.skillexchange.backend.talent.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;
import place.skillexchange.backend.comment.dto.CommentDto;
import place.skillexchange.backend.comment.entity.Comment;
import place.skillexchange.backend.file.entity.File;
import place.skillexchange.backend.file.service.FileServiceImpl;
import place.skillexchange.backend.talent.dto.TalentDto;
import place.skillexchange.backend.talent.entity.Place;
import place.skillexchange.backend.talent.entity.SubjectCategory;
import place.skillexchange.backend.talent.entity.Talent;
import place.skillexchange.backend.talent.repository.PlaceRepository;
import place.skillexchange.backend.talent.repository.SubjectCategoryRepository;
import place.skillexchange.backend.talent.repository.TalentRepository;
import place.skillexchange.backend.user.entity.User;
import place.skillexchange.backend.user.repository.UserRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Slf4j
class TalentServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TalentRepository talentRepository;

    @Mock
    private PlaceRepository placeRepository;

    @Mock
    private SubjectCategoryRepository categoryRepository;

    @Mock
    private FileServiceImpl fileService;

    @InjectMocks
    private TalentServiceImpl talentService;

    @Test
    @DisplayName("재능교환 게시물 생성 테스트")
    public void testRegister() throws IOException {
        //Given
        Long noticeId = 1L;
        String userId = "testUser";
        String writer = "testUser";
        String title = "testTitle";
        String content = "testContent";
        String placeName = "testPlace";
        String teachingSubject = "testTeachingSubject";
        String teachedSubject = "testTeachedSubject";
        String ageGroup = "25~30";
        String week = "월요일";
        String img = "img.jpg";
        String imgUrl = "https://.../img1.jpg";

        User user = User.builder().id(userId).build();
        Place place = new Place(1L, placeName);
        SubjectCategory teachingSubjectCategory = new SubjectCategory(7L, teachingSubject, new SubjectCategory(1L, "parentCategory1", null));
        SubjectCategory teachedSubjectCategory = new SubjectCategory(19L, teachedSubject, new SubjectCategory(2L, "parentCategory2", null));
        Talent talent = Talent.builder().id(noticeId).writer(user).place(place).teachingSubject(teachingSubjectCategory).teachedSubject(teachedSubjectCategory).title(title).content(content).ageGroup(ageGroup).week(week).hit(0L).build();
        //예상으로 반환되는 객체
        List<File> files = new ArrayList<>();
        File file = File.builder().oriName(img).fileUrl(imgUrl).build();
        files.add(file);

        // MultipartFile을 저장할 리스트 생성
        List<MultipartFile> multipartFiles = new ArrayList<>();
        multipartFiles.add(new MockMultipartFile(img.substring(0, img.lastIndexOf('.')), img, "image/jpeg", new byte[0]));

        TalentDto.TalentRegisterRequest request = TalentDto.TalentRegisterRequest.builder()
                .writer(writer)
                .title(title)
                .content(content)
                .placeName(placeName)
                .teachingSubject(teachingSubject)
                .teachedSubject(teachedSubject)
                .ageGroup(ageGroup)
                .week(week).build();

        // 현재 인증된 사용자 설정
        Authentication authentication = new TestingAuthenticationToken(userId, null, "ROLE_USER");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // userRepository의 동작을 모의화
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        //placeRepository의 동작을 모의화
        when(placeRepository.findByPlaceName(placeName)).thenReturn(Optional.of(place));
        //categoryRepository의 동작을 모의화
        when(categoryRepository.findBySubjectName(teachingSubject)).thenReturn(Optional.of(teachingSubjectCategory));
        //categoryRepository의 동작을 모의화
        when(categoryRepository.findBySubjectName(teachedSubject)).thenReturn(Optional.of(teachedSubjectCategory));
        //talentRepository의 동작을 모의화
        when(talentRepository.save(any(Talent.class))).thenReturn(talent);
        // fileService의 동작을 모의화
        when(fileService.registerTalentImg(multipartFiles, talent)).thenReturn(files);

        //When
        TalentDto.TalentRegisterResponse response = talentService.register(request,multipartFiles);

        // Then
        assertNotNull(response);
        assertThat(userId).isEqualTo(response.getWriter());
        assertThat(noticeId).isEqualTo(response.getId());
        assertThat(title).isEqualTo(response.getTitle());
        assertThat(content).isEqualTo(response.getContent());
        assertThat(placeName).isEqualTo(response.getPlaceName());
        assertThat(teachingSubject).isEqualTo(response.getTeachingSubject());
        assertThat(teachedSubject).isEqualTo(response.getTeachedSubject());
        assertThat(ageGroup).isEqualTo(response.getAgeGroup());
        assertThat(week).isEqualTo(response.getWeek());
        assertThat(files.get(0).getFileUrl()).isEqualTo(response.getImgUrl().get(0));
        assertThat(201).isEqualTo(response.getReturnCode());
        assertThat("재능교환 게시물이 등록되었습니다.").isEqualTo(response.getReturnMessage());

        verify(userRepository).findById(userId);
        verify(placeRepository).findByPlaceName(placeName);
        verify(categoryRepository).findBySubjectName(teachingSubject);
        verify(categoryRepository).findBySubjectName(teachedSubject);
        verify(talentRepository).save(any(Talent.class));
        // fileService.registerNoticeImg가 올바른 파라미터와 함께 호출되었는지 확인
        verify(fileService).registerTalentImg(multipartFiles, talent);
    }
}