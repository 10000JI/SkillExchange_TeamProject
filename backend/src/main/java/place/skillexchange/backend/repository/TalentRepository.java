package place.skillexchange.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import place.skillexchange.backend.entity.SubjectCategory;
import place.skillexchange.backend.entity.Talent;

import java.util.List;

public interface TalentRepository extends JpaRepository<Talent, Long>, CustomTalentRepository {


    // 조회수 증가를 위한 업데이트 쿼리
    @Transactional
    @Modifying
    @Query("UPDATE Talent n SET n.hit = n.hit + 1 WHERE n.id = :talentId")
    void updateHit(Long talentId);

}
