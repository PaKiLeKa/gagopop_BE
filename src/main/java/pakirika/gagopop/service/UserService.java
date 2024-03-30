package pakirika.gagopop.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pakirika.gagopop.dto.UserProfileDTO;
import pakirika.gagopop.entity.Stamp;
import pakirika.gagopop.entity.UserEntity;
import pakirika.gagopop.entity.UserStamp;
import pakirika.gagopop.repository.TogoListRepository;
import pakirika.gagopop.repository.UserRepository;
import pakirika.gagopop.repository.UserStampRepository;
import pakirika.gagopop.repository.WishlistRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final WishlistRepository wishlistRepository;

    private final TogoListRepository togoListRepository;

    private final UserStampRepository userStampRepository;



    //회원 전체 정보 - nickname, email, stamp 총 개수, wishlist 총 개수, togo list 총 개수

    public UserProfileDTO getUserProfile(UserEntity userEntity){
        UserProfileDTO userProfile=new UserProfileDTO();

        Long wishListTotal=wishlistRepository.countByUserEntity( userEntity );
        Long togoListTotal=togoListRepository.countByUserEntity( userEntity );
        Long stampTotal=userStampRepository.countByUserEntity( userEntity );

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
        user.ifPresent( value ->value.setNickname(newNickname) );

    }

    //Todo
    //유저 stamp 전체 가져오기
    public List<Stamp> findUsersStampAll(UserEntity userEntity){

        List<Stamp> stamps=userStampRepository.findByUserEntity( userEntity );

        return stamps;
    }



}
