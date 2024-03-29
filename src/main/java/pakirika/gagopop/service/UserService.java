package pakirika.gagopop.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pakirika.gagopop.dto.UserDTO;
import pakirika.gagopop.dto.UserProfileDTO;
import pakirika.gagopop.entity.UserEntity;
import pakirika.gagopop.repository.StampListRepository;
import pakirika.gagopop.repository.TogoListRepository;
import pakirika.gagopop.repository.UserRepository;
import pakirika.gagopop.repository.WishlistRepository;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final WishlistRepository wishlistRepository;

    private final TogoListRepository togoListRepository;

    private final StampListRepository stampListRepository;



    //회원 전체 정보 - nickname, email, stamp 총 개수, wishlist 총 개수, togo list 총 개수

    public UserProfileDTO getUserProfile(UserEntity userEntity){
        UserProfileDTO userProfile=new UserProfileDTO();

        Long wishListTotal=wishlistRepository.countByUserEntity( userEntity );
        Long togoListTotal=togoListRepository.countByUserEntity( userEntity );
        Long stampTotal=stampListRepository.countByUserEntity( userEntity );

        userProfile.setEmail( userEntity.getEmail() );
        userProfile.setNickname( userEntity.getNickname() );
        userProfile.setWishlistTotal( wishListTotal );
        userProfile.setTogolistTotal( togoListTotal );
        userProfile.setStampTotal( stampTotal );

        return userProfile;

    }

    //유저 닉네임수정



}
