package place.skillexchange.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import place.skillexchange.backend.entity.File;
import place.skillexchange.backend.entity.Notice;

import java.util.List;
import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice,Integer> {
    Optional<Notice> findById(Long noticeId);

    void deleteById(Long noticeId);
}
