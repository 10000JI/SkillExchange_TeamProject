package place.skillexchange.backend.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import place.skillexchange.backend.dto.NoticeDto;
import place.skillexchange.backend.dto.TalentDto;
import place.skillexchange.backend.entity.QTalent;
import place.skillexchange.backend.entity.Talent;

import java.util.List;

@RequiredArgsConstructor
public class CustomTalentRepositoryImpl implements CustomTalentRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<TalentDto.ListResponse> findAllWithPagingAndSearch(String keyword, Pageable pageable, Long subjectCategoryId) {
        QTalent qTalent = QTalent.talent;

        BooleanExpression predicate = qTalent.isNotNull();
        if (subjectCategoryId != null) {
            predicate = predicate.and(qTalent.teachedSubject.id.eq(subjectCategoryId));
        }

        if (keyword != null && !keyword.isEmpty()) {
            predicate = predicate.and(qTalent.content.containsIgnoreCase(keyword)
                    .or(qTalent.teachedSubject.subjectName.containsIgnoreCase(keyword))
                    .or(qTalent.teachingSubject.subjectName.containsIgnoreCase(keyword))
                    .or(qTalent.place.placeName.containsIgnoreCase(keyword)));
        }

        List<TalentDto.ListResponse> talents = queryFactory
                .select(Projections.constructor(TalentDto.ListResponse.class, qTalent))
                .from(qTalent)
                .where(predicate)
                .orderBy(qTalent.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long totalCount = queryFactory
                .selectFrom(qTalent)
                .where(predicate)
                .fetchCount();

        return new PageImpl<>(talents, pageable, totalCount);
    }
}