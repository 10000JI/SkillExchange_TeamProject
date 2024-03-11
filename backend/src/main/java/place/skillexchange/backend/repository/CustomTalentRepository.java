package place.skillexchange.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import place.skillexchange.backend.dto.TalentDto;
import place.skillexchange.backend.entity.Talent;

import java.util.List;

public interface CustomTalentRepository {
    Page<TalentDto.ListResponse> findAllWithPagingAndSearch(String keyword, Pageable pageable, Long subjectCategoryId);
}
