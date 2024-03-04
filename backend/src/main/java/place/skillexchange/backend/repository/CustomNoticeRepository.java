package place.skillexchange.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import place.skillexchange.backend.dto.NoticeDto;

public interface CustomNoticeRepository {
    //공지사항 페이징 처리 및 검색어 처리
    Page<NoticeDto.ListResponse> findNoticesWithPagingAndKeyword(String keyword, Pageable pageable);
}
