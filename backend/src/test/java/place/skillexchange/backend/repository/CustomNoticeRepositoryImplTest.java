package place.skillexchange.backend.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import place.skillexchange.backend.notice.entity.Notice;
import place.skillexchange.backend.notice.repository.NoticeRepository;
import place.skillexchange.backend.user.entity.User;
import place.skillexchange.backend.user.repository.UserRepository;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
class CustomNoticeRepositoryImplTest {

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("공지사항 더미 데이터")
    public void insetDummies() {
        Optional<User> userOptional = userRepository.findById("admin");

        userOptional.ifPresent(user -> {
            IntStream.rangeClosed(2, 20).forEach(i -> {
                Notice notice = Notice.builder()
                        .title("공지사항입니다 ..." + i)
                        .content("내용입니다 ..." + i)
                        .writer(user)
                        .hit(0L)
                        .build();
                noticeRepository.save(notice);
            });
        });
    }
}