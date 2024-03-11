package place.skillexchange.backend.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import place.skillexchange.backend.entity.File;
import place.skillexchange.backend.entity.Notice;

import java.util.List;
import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice,Long>, CustomNoticeRepository{
    Optional<Notice> findById(Long noticeId);

    void deleteById(Long noticeId);

    // 조회수 증가를 위한 업데이트 쿼리
    @Transactional
    @Modifying
    @Query("UPDATE Notice n SET n.hit = n.hit + 1 WHERE n.id = :noticeId")
    void updateHit(Long noticeId);
}

