package place.skillexchange.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import place.skillexchange.backend.dto.NoticeDto;
import place.skillexchange.backend.dto.TalentDto;
import place.skillexchange.backend.entity.*;
import place.skillexchange.backend.exception.BoardNotFoundException;
import place.skillexchange.backend.exception.PlaceNotFoundException;
import place.skillexchange.backend.exception.SubjectCategoryNotFoundException;
import place.skillexchange.backend.exception.UserNotFoundException;
import place.skillexchange.backend.repository.PlaceRepository;
import place.skillexchange.backend.repository.SubjectCategoryRepository;
import place.skillexchange.backend.repository.TalentRepository;
import place.skillexchange.backend.repository.UserRepository;
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

    //재능교환 게시물 생성
    @Override
    public TalentDto.RegisterResponse register(TalentDto.RegisterRequest dto, List<MultipartFile> multipartFiles) throws IOException {
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
        return new TalentDto.RegisterResponse(user,talent,files,201,"재능교환 게시물이 등록되었습니다.");
    }

    //게시물 올린 글쓴이의 프로필 정보 불러오기
    @Override
    public TalentDto.writerInfoResponse writerInfo(Long writerId) {
        Talent talent = talentRepository.findById(writerId).orElseThrow(() -> new BoardNotFoundException("존재하지 않는 게시물 번호입니다: " + writerId));
        User user = userRepository.findById(talent.getWriter().getId()).orElseThrow(() -> new UsernameNotFoundException("해당 유저를 찾지 못했습니다: " + talent.getWriter().getId()));
        return new TalentDto.writerInfoResponse(user);
    }

    // 조회수 증가를 위한 업데이트
    @Transactional
    public void increaseHit(Long talentId) {
        talentRepository.updateHit(talentId);
    }

    // 게시물 조회
    @Override
    @Transactional(readOnly = true)
    public TalentDto.ReadResponse read(Long talentId) {
        Talent talent = talentRepository.findById(talentId)
                .orElseThrow(() -> new BoardNotFoundException("존재하지 않는 게시물 번호입니다: " + talentId));
        increaseHit(talentId);
        return new TalentDto.ReadResponse(talent);
    }

    // 게시물 수정
    @Override
    public TalentDto.UpdateResponse update(TalentDto.UpdateRequest dto, List<MultipartFile> multipartFiles, Long talentId) throws IOException {
        String id = securityUtil.getCurrentMemberUsername();
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("해당 유저를 찾지 못했습니다: " + id));
        if (!Objects.equals(id, dto.getWriter())) {
            throw new UserNotFoundException("로그인한 회원 정보와 글쓴이가 다릅니다.");
        }
        Talent talent = talentRepository.findById(talentId)
                .orElseThrow(() -> new BoardNotFoundException("존재하지 않는 게시물 번호입니다: " + talentId));
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


        return new TalentDto.UpdateResponse(user, talent, files, 200, "재능교환 게시물이 수정되었습니다.");
    }

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
}
