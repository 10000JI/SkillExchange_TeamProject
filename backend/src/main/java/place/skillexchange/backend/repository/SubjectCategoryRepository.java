package place.skillexchange.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import place.skillexchange.backend.entity.SubjectCategory;

import java.util.List;


public interface SubjectCategoryRepository extends JpaRepository<SubjectCategory,Long> {
    @Query("SELECT c FROM SubjectCategory c LEFT JOIN c.parent p ORDER BY p.id ASC NULLS FIRST, c.id ASC")
    List<SubjectCategory> findAllOrderByParentIdAscNullsFirstCategoryIdAsc();
}
