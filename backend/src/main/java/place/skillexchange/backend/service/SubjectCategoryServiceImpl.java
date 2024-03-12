package place.skillexchange.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import place.skillexchange.backend.dto.SubjectCategoryDto;
import place.skillexchange.backend.entity.SubjectCategory;
import place.skillexchange.backend.repository.SubjectCategoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubjectCategoryServiceImpl implements SubjectCategoryService {
    private final SubjectCategoryRepository subjectCategoryRepository;

    @Override
    public List<SubjectCategoryDto.CategoryListResponse> findAll() {
        List<SubjectCategory> subjectCategories = subjectCategoryRepository.findAllOrderByParentIdAscNullsFirstCategoryIdAsc();
        return SubjectCategoryDto.CategoryListResponse.toDtoList(subjectCategories);
    }
}
