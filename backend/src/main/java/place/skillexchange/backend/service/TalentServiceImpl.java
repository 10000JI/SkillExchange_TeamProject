package place.skillexchange.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import place.skillexchange.backend.dto.TalentDto;
import place.skillexchange.backend.entity.*;
import place.skillexchange.backend.exception.BoardNotFoundException;
import place.skillexchange.backend.exception.PlaceNotFoundException;
import place.skillexchange.backend.exception.SubjectCategoryNotFoundException;
import place.skillexchange.backend.exception.UserNotFoundException;
import place.skillexchange.backend.repository.*;
import place.skillexchange.backend.util.SecurityUtil;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TalentServiceImpl implements TalentService {

    private final SecurityUtil securityUtil;
    private final TalentRepository talentRepository;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;
    private final SubjectCategoryRepository categoryRepository;
    private final FileService fileService;
    private final TalentScrapRepository scrapRepository;

    //재능교환 게시물 생성
    @Override
    public TalentDto.TalentRegisterResponse register(TalentDto.TalentRegisterRequest dto, List<MultipartFile> multipartFiles) throws IOException {
        String id = securityUtil.getCurrentMemberUsername();
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("해당 유저를 찾지 못했습니다: " + id));
        if (!Objects.equals(id, dto.getWriter())) {
            throw new UserNotFoundException("로그인한 회원 정보와 글쓴이가 다릅니다.");
        }
        Place place = placeRepository.findByPlaceName(dto.getPlaceName()).orElseThrow(() -> new PlaceNotFoundException("해당 장소는 등록되지 않은 장소입니다: "+dto.getPlaceName()));
        SubjectCategory teachingSubject = categoryRepository.findBySubjectName(dto.getTeachingSubject()).orElseThrow(() -> new SubjectCategoryNotFoundException("해당 분야는 등록되지 않은 분야입니다: " + dto.getTeachingSubject()));
        SubjectCategory teachedSubject = categoryRepository.findBySubjectName(dto.getTeachedSubject()).orElseThrow(() -> new SubjectCategoryNotFoundException("해당 분야는 등록되지 않은 분야입니다: " + dto.getTeachedSubject()));

        Talent talent = talentRepository.save(dto.toEntity(user, place, teachingSubject, teachedSubject));

        List<File> files = null;
        System.out.println("MultiPartFiles:  "+multipartFiles);
        if (multipartFiles != null) {
            files = fileService.registerTalentImg(multipartFiles,talent);
        }
        return new TalentDto.TalentRegisterResponse(user,talent,files,201,"재능교환 게시물이 등록되었습니다.");
    }

    //게시물 올린 글쓴이의 프로필 정보 불러오기
    @Override
    public TalentDto.WriterInfoResponse writerInfo(Long writerId) {
        Talent talent = talentRepository.findById(writerId).orElseThrow(() -> new BoardNotFoundException("존재하지 않는 게시물 번호입니다: " + writerId));
        User user = userRepository.findById(talent.getWriter().getId()).orElseThrow(() -> new UsernameNotFoundException("해당 유저를 찾지 못했습니다: " + talent.getWriter().getId()));
        return new TalentDto.WriterInfoResponse(user);
    }

    // 조회수 증가를 위한 업데이트
    @Transactional
    public void increaseHit(Long talentId) {
        talentRepository.updateHit(talentId);
    }

    // 게시물 조회
    @Override
    @Transactional(readOnly = true)
    public TalentDto.TalentReadResponse read(Long talentId) {
        Talent talent = talentRepository.findById(talentId)
                .orElseThrow(() -> new BoardNotFoundException("존재하지 않는 게시물 번호입니다: " + talentId));
        increaseHit(talentId);
        return new TalentDto.TalentReadResponse(talent);
    }

    // 게시물 수정
    @Override
    public TalentDto.TalentUpdateResponse update(TalentDto.TalentUpdateRequest dto, List<MultipartFile> multipartFiles, Long talentId) throws IOException {
        String id = securityUtil.getCurrentMemberUsername();
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("해당 유저를 찾지 못했습니다: " + id));
        Talent talent = talentRepository.findById(talentId)
                .orElseThrow(() -> new BoardNotFoundException("존재하지 않는 게시물 번호입니다: " + talentId));
        if (!Objects.equals(id, dto.getWriter()) || !Objects.equals(id,talent.getWriter().getId()) || !Objects.equals(dto.getWriter(),talent.getWriter().getId())) {
            throw new UserNotFoundException("로그인한 회원 정보와 글쓴이가 다릅니다.");
        }
        Place place = null;
        if (!talent.getPlace().getPlaceName().equals(dto.getPlaceName())) {
            place = placeRepository.findByPlaceName(dto.getPlaceName()).orElseThrow(() -> new PlaceNotFoundException("해당 장소는 등록되지 않은 장소입니다: " + dto.getPlaceName()));
        }
        SubjectCategory teachedSubject = null;
        if (!talent.getTeachedSubject().getSubjectName().equals(dto.getTeachedSubject())) {
            teachedSubject = categoryRepository.findBySubjectName(dto.getTeachedSubject()).orElseThrow(() -> new SubjectCategoryNotFoundException("해당 분야는 등록되지 않은 분야입니다: " + dto.getTeachedSubject()));
        }
        SubjectCategory teachingSubject = null;
        if(!talent.getTeachingSubject().getSubjectName().equals(dto.getTeachingSubject())) {
            teachingSubject = categoryRepository.findBySubjectName(dto.getTeachingSubject()).orElseThrow(() -> new SubjectCategoryNotFoundException("해당 분야는 등록되지 않은 분야입니다: " + dto.getTeachingSubject()));
        }
        talent.changeNotice(dto, place, teachedSubject, teachingSubject);

        List<File> files = fileService.updateTalentImg(dto.getImgUrl(), multipartFiles, talent);


        return new TalentDto.TalentUpdateResponse(user, talent, files, 200, "재능교환 게시물이 수정되었습니다.");
    }

    //게시물 삭제
    @Override
    public TalentDto.ResponseBasic delete(Long talentId) {
        String id = securityUtil.getCurrentMemberUsername();
        Optional<Talent> deleteTalent = talentRepository.findById(talentId);
        if (deleteTalent.isPresent()) {
            if (!Objects.equals(id, deleteTalent.get().getWriter().getId())) {
                throw new UserNotFoundException("로그인한 회원 정보와 글쓴이가 다릅니다.");
            }
            talentRepository.deleteById(talentId);
            return new TalentDto.ResponseBasic(200, "재능교환 게시물이 성공적으로 삭제되었습니다.");
        } else {
            throw new BoardNotFoundException("존재하지 않는 게시물 번호입니다: " + talentId);
        }
    }

    //게시물 목록
    @Override
    public Page<TalentDto.TalentListResponse> list(int limit, int skip, String keyword, Long subjectCategoryId) {
        Pageable pageable = PageRequest.of(skip, limit);
        return talentRepository.findAllWithPagingAndSearch(keyword, pageable, subjectCategoryId);
    }

    //게시물 스크랩
    @Override
    public TalentDto.ResponseBasic scrap(Long talentId) {
        String id = securityUtil.getCurrentMemberUsername();
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("해당 유저를 찾지 못했습니다: " + id));
        if (scrapRepository.findByTalentId(talentId, id) != null) {
            throw new BoardNotFoundException("이미 스크랩한 게시물 입니다.");
        }
        Talent talent = talentRepository.findById(talentId).orElseThrow(() -> new BoardNotFoundException("존재하지 않는 게시물 번호입니다: " + talentId));
        TalentScrap scrap = TalentScrap.of(user, talent);
        scrapRepository.save(scrap);

        return new TalentDto.ResponseBasic(201,"스크랩이 완료되었습니다.");
    }
}
