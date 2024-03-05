package place.skillexchange.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import place.skillexchange.backend.dto.SubjectCategoryDto;
import place.skillexchange.backend.service.SubjectCategoryService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/subjectCategory/")
public class SubjectCategoryController {
    private final SubjectCategoryService categoryService;

    @GetMapping("/list")
    public List<SubjectCategoryDto.ListResponse> list() {
        return categoryService.findAll();
    }
}
