package place.skillexchange.backend.service;

import place.skillexchange.backend.dto.SubjectCategoryDto;
import place.skillexchange.backend.entity.SubjectCategory;

import java.util.List;

public interface SubjectCategoryService {

    public List<SubjectCategoryDto.ListResponse> findAll();
}
