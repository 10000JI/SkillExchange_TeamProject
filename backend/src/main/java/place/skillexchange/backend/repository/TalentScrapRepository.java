package place.skillexchange.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import place.skillexchange.backend.entity.TalentScrap;
import place.skillexchange.backend.entity.TalentScrapId;

import java.util.List;

@Repository
public interface TalentScrapRepository extends JpaRepository<TalentScrap, TalentScrapId> {

    @Query("SELECT ts FROM TalentScrap ts WHERE ts.talent.id = :talentId AND ts.user.id = :userId")
    TalentScrap findByTalentId(Long talentId, String userId);

}
