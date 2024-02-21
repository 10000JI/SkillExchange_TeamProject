package place.skillexchange.backend.auth.services;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import place.skillexchange.backend.entity.RefreshToken;
import place.skillexchange.backend.entity.User;
import place.skillexchange.backend.exception.UserUnAuthorizedException;
import place.skillexchange.backend.repository.RefreshTokenRepository;
import place.skillexchange.backend.repository.UserRepository;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final UserRepository userRepository;

    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * refreshToken 생성
     */
    @Transactional
    public RefreshToken createRefreshToken(String id) {
        //사용자 이름이 존재하면 User 객체 반환, 없다면 사용자를 찾을 수 없다는 예외
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("해당 유저를 찾지 못했습니다 : " + id));

        //user의 refreshToken을 가져와 RefreshToken 객체 추출
        RefreshToken refreshToken = user.getRefreshToken();

        //refreshToken이 NULL이라면 refreshToken을 새롭게 만든다
        if (refreshToken == null) {
            refreshToken = RefreshToken.builder()
                    //refreshToken은 UUID로 생성
                    .refreshToken(UUID.randomUUID().toString())
                    //만료일은 2분 (실제로는 2주 정도로 설정)
                    .expirationTime(new Date((new Date()).getTime() + 2 * 60 * 1000))
                    .user(user)
                    .build();

            refreshTokenRepository.save(refreshToken);
        } else {
            refreshToken.changeRefreshTokenExp(new Date((new Date()).getTime() + 2 * 60 * 1000));
            //refreshTokenRepository.save(refreshToken);
        }

        return refreshToken;
    }

//    /**
//     * refreshToken 확인
//     */
//    public RefreshToken verifyRefreshToken(String refreshToken) {
//        RefreshToken refToken = refreshTokenRepository.findByRefreshToken(refreshToken)
//                .orElseThrow(() -> new RuntimeException("리프레시 토큰을 찾을 수가 없습니다.!"));
//
//        // refreshToken의 만료시간이 현재 시간보다 작다면 refreshToken 삭제
//        if (refToken.getExpirationTime().compareTo(Date.from(Instant.now())) < 0) {
//            refreshTokenRepository.delete(refToken);
//            throw new UserUnAuthorizedException("만료된 로그인 정보 입니다. 재로그인을 하세요.");
//        }
//
//        return refToken;
//    }

    /**
     * refreshToken 확인
     */
    public RefreshToken verifyRefreshToken(String refreshToken) {
        RefreshToken refToken = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new UserUnAuthorizedException("Refresh Token을 찾을 수 없습니다."));

        //refreshToken의 만료시간이 현재 시간보다 작다면 refreshToken 삭제
        if (refToken.getExpirationTime().compareTo(Date.from(Instant.now())) < 0) {
            refreshTokenRepository.delete(refToken);
            throw new UserUnAuthorizedException("Refresh Token이 만료되었습니다.");
        }

        return refToken;
    }
}
