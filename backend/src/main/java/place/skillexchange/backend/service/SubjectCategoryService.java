package place.skillexchange.backend.service;

import place.skillexchange.backend.dto.SubjectCategoryDto;

import java.util.List;

public interface SubjectCategoryService {

    public List<SubjectCategoryDto.CategoryListResponse> findAll();
}
