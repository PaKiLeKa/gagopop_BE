package pakirika.gagopop.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pakirika.gagopop.dto.UserProfileDTO;
import pakirika.gagopop.entity.UserEntity;
import pakirika.gagopop.jwt.JWTUtil;
import pakirika.gagopop.repository.UserRepository;
import pakirika.gagopop.service.UserService;

import java.util.Optional;


@RestController
@RequiredArgsConstructor
@Transactional
public class UserController {

    private final UserService userService;

    //회원 전체 정보 -username, email, stamp 총 개수, wishlist 총 개수, togo list 총 개수

    @GetMapping("/user/profile")
    public ResponseEntity<?> userProfile(HttpServletRequest request){


        Optional<UserEntity> optionalUser=userService.findUser( request );

        if(optionalUser.isEmpty()){
            return ResponseEntity.status( HttpStatus.NOT_FOUND ).body( "User not found" );
        }

        UserProfileDTO userProfile=userService.getUserProfile( optionalUser.get() );

        return ResponseEntity.ok( userProfile );
    }

    //유저 닉네임수정
    @PostMapping("/user/profile/edit")
    public ResponseEntity editUserNickname(HttpServletRequest request, @RequestParam("nickname") String newNickName){

        Optional<UserEntity> optionalUser=userService.findUser( request );
        if(optionalUser.isEmpty()){
            return ResponseEntity.status( HttpStatus.UNAUTHORIZED).body("User not found"); //유저를 찾을 수 없는 경우
        }

        userService.updateUserNickName(optionalUser.get(), newNickName);

        return ResponseEntity.ok("Updated user nickname");
    }

}
