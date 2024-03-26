package pakirika.gagopop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import pakirika.gagopop.dto.*;
import pakirika.gagopop.entity.UserEntity;
import pakirika.gagopop.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User=super.loadUser( userRequest );
        System.out.println( oAuth2User );

        String registrationId=userRequest.getClientRegistration().getRegistrationId(); //제공자 구분하기!

        OAuth2Response oAuth2Response=null;
        if (registrationId.equals( "naver" )) {

            oAuth2Response=new NaverResponse( oAuth2User.getAttributes() );
        } else if (registrationId.equals( "google" )) {

            oAuth2Response=new GoogleResponse( oAuth2User.getAttributes() );
        } else {

            return null;
        }

        String username=oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId(); //이걸로 해당 유저가 DB에 이미 존재하는지 확인해야함!

        UserEntity existData=userRepository.findByUsername( username );

        if(existData == null){
            UserEntity userEntity =new UserEntity();
            userEntity.setUsername( username );
            userEntity.setEmail( oAuth2Response.getEmail() );
            userEntity.setName( oAuth2Response.getName() );
            userEntity.setRole( "ROLE_USER" );

            userRepository.save( userEntity );

            UserDTO userDTO=new UserDTO();
            userDTO.setUsername( username );
            userDTO.setName( oAuth2Response.getName() );
            userDTO.setRole( "ROLE_USER" );

            return new CustomOAuth2User( userDTO );

        }else{

            existData.setEmail( oAuth2Response.getEmail() ); //있으면 받은 정보로 업데이트
            existData.setName( oAuth2Response.getName() );

            userRepository.save( existData );

            UserDTO userDTO=new UserDTO();
            userDTO.setUsername( existData.getUsername() );
            userDTO.setName( oAuth2Response.getName() );
            userDTO.setRole( existData.getRole() );
            //name은 새로 응답받은 데이터로 업데이트 할 것임

            return new CustomOAuth2User( userDTO );

        }


/*      //JWT 적용 전
        UserEntity existData=userRepository.findByUsername( username );

        String role = null;
        if(existData == null){//없으면 등록
            UserEntity userEntity =new UserEntity();
            userEntity.setUsername( username );
            userEntity.setEmail( oAuth2Response.getEmail() );
            userEntity.setRole( "ROLE_USER" );

            userRepository.save( userEntity );
        }else {

            role = existData.getRole(); //기존 role 그대로 사용
            existData.setEmail( oAuth2Response.getEmail() ); //있으면 받은 정보로 업데이트
            userRepository.save( existData );

        }

        //추후 작성
        return new CustomOAuth2User(oAuth2Response, role);

    }*/
    }
}