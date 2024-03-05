package place.skillexchange.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import place.skillexchange.backend.entity.Notice;
import place.skillexchange.backend.entity.SubjectCategory;
import place.skillexchange.backend.util.NestedConvertHelper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SubjectCategoryDto {

    /**
     * 카테고리 목록 응답 Dto
     */
    @Getter
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ListResponse {
        private Long id;
        private String name;
        private List<ListResponse> children;

        public static List<ListResponse> toDtoList(List<SubjectCategory> categories) {
            NestedConvertHelper helper = NestedConvertHelper.newInstance(
                    categories,
                    c -> new ListResponse(c.getId(), c.getSubjectName(), new ArrayList<>()),
                    c -> c.getParent(),
                    c -> c.getId(),
                    d -> d.getChildren());
            return helper.convert();
        }
    }
}
