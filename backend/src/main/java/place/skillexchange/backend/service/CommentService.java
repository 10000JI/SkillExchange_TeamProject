package place.skillexchange.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import place.skillexchange.backend.dto.CommentDto;
import place.skillexchange.backend.entity.Comment;
import place.skillexchange.backend.entity.Notice;
import place.skillexchange.backend.entity.User;
import place.skillexchange.backend.exception.NoticeNotFoundException;
import place.skillexchange.backend.exception.UserNotFoundException;
import place.skillexchange.backend.repository.CommentRepository;
import place.skillexchange.backend.repository.NoticeRepository;
import place.skillexchange.backend.repository.UserRepository;
import place.skillexchange.backend.util.SecurityUtil;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final NoticeRepository noticeRepository;
    private final CommentRepository commentRepository;
    private final SecurityUtil securityUtil;
    private final UserRepository userRepository;


    public List<CommentDto.ViewResponse> findCommentsByNoticeId(Long noticeId) {
        noticeRepository.findById(noticeId).orElseThrow(() -> new NoticeNotFoundException("존재하지 않는 게시물 번호입니다: " + noticeId));
        return convertNestedStructure(commentRepository.findCommentByNoticeId(noticeId));
    }

    private List<CommentDto.ViewResponse> convertNestedStructure(List<Comment> comments) {
        List<CommentDto.ViewResponse> result = new ArrayList<>();
        Map<Long, CommentDto.ViewResponse> map = new HashMap<>();
        comments.stream().forEach(c -> {
            CommentDto.ViewResponse dto = CommentDto.ViewResponse.entityToDto(c);
            map.put(dto.getId(), dto);
            if(c.getParent() != null) map.get(c.getParent().getId()).getChildren().add(dto);
            else result.add(dto);
        });
        return result;
    }

    public CommentDto.RegisterResponse createComment(CommentDto.RegisterRequest dto) {
        String id = securityUtil.getCurrentMemberUsername();
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("해당 유저를 찾지 못했습니다 : " + id));
        if (!Objects.equals(id, dto.getWriter())) {
            throw new UserNotFoundException("로그인한 회원 정보와 글쓴이가 다릅니다.");
        }
        Notice notice = noticeRepository.findById(dto.getNoticeId()).orElseThrow(() -> new NoticeNotFoundException("존재하지 않는 게시물 번호입니다: " + dto.getNoticeId()));

        Comment comment = dto.getParentId() != null ?
                commentRepository.findById(dto.getParentId())
                        .orElseThrow(() -> new NoticeNotFoundException("존재하지 않는 댓글 번호입니다: " + dto.getParentId())) : null;
        Comment saveComment = commentRepository.save(dto.toEntity(user, notice, comment));
        return new CommentDto.RegisterResponse(saveComment,200,"댓글이 성공적으로 등록되었습니다.");
    }
}
