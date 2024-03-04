package place.skillexchange.backend.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import place.skillexchange.backend.entity.Notice;
import place.skillexchange.backend.entity.User;

import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class NoticeRepositoryImplTest {

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("공지사항 더미 데이터")
    public void insetDummies() {
        Optional<User> userOptional = userRepository.findById("alswl3359");

        userOptional.ifPresent(user -> {
            IntStream.rangeClosed(1, 10).forEach(i -> {
                Notice notice = Notice.builder()
                        .title("공지테스트 ..." + i)
                        .content("내용테스트..." + i)
                        .writer(user)
                        .build();
                noticeRepository.save(notice);
            });
        });
    }
}