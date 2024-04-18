package pakirika.gagopop.service;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pakirika.gagopop.dto.UserProfileDTO;
import pakirika.gagopop.entity.Stamp;
import pakirika.gagopop.entity.UserEntity;
import pakirika.gagopop.jwt.JWTUtil;
import pakirika.gagopop.repository.TogoListRepository;
import pakirika.gagopop.repository.UserRepository;
import pakirika.gagopop.repository.StampRepository;
import pakirika.gagopop.repository.WishlistRepository;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final WishlistRepository wishlistRepository;

    private final TogoListRepository togoListRepository;

    private final StampRepository stampRepository;

    private final JWTUtil jwtUtil;


    //회원 전체 정보 - nickname, email, stamp 총 개수, wishlist 총 개수, togo list 총 개수

    public UserProfileDTO getUserProfile(UserEntity userEntity) {
        UserProfileDTO userProfile=new UserProfileDTO();

        Long wishListTotal=wishlistRepository.countByUserEntity( userEntity );
        Long togoListTotal=togoListRepository.countByUserEntity( userEntity );
        Long stampTotal=stampRepository.countByUserEntity( userEntity );

        userProfile.setEmail( userEntity.getEmail() );
        userProfile.setNickname( userEntity.getNickname() );
        userProfile.setWishlistTotal( wishListTotal );
        userProfile.setTogolistTotal( togoListTotal );
        userProfile.setStampTotal( stampTotal );

        return userProfile;

    }

    //유저 닉네임수정
    @Transactional
    public void updateUserNickName(UserEntity userEntity, String newNickname) {

        Optional<UserEntity> user=userRepository.findById( userEntity.getId() );
        user.ifPresent( value -> value.setNickname( newNickname ) );

    }

    //Todo
    //유저 stamp 가져옴! -> API로 어떻게 넘길지 생각해보기
    public List<Stamp> findUsersStampAll(UserEntity userEntity) {

        List<Stamp> stamps=stampRepository.findByUserEntity( userEntity );

        return stamps;
    }

    public Optional<UserEntity> findUser(HttpServletRequest request) {

        String authorization=null;

        Cookie[] cookies=request.getCookies();
        if (cookies != null) { // null 체크 추가
            for (Cookie cookie : cookies) {
                if (cookie != null && cookie.getName().equals( "Authorization" )) {
                    authorization=cookie.getValue();
                } else if (cookie != null && cookie.getName().equals( "authorization" )) {
                    authorization=cookie.getValue();
                }
            }
        }

        //Authorization 헤더 검증
        if (authorization == null) {
            System.out.println( "token null" );
            //조건이 해당되면 메소드 종료한다.
            return Optional.empty();
        }

        String token=authorization;

        // JWT 토큰에서 유저 이름 가져오기
        String username=jwtUtil.getUsername( token );
        UserEntity userEntity = new UserEntity();
        if(username != null) {
            userEntity=userRepository.findByUsername( jwtUtil.getUsername( token ) );
            if (userEntity == null) {
                return Optional.empty();
            }
        }

        return Optional.of( userEntity );
    }
}
