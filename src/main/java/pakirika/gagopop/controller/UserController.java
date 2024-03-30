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
    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;

    //회원 전체 정보 -username, email, stamp 총 개수, wishlist 총 개수, togo list 총 개수

    @GetMapping("/user/profile")
    public ResponseEntity<?> userProfile(HttpServletRequest request){

        //String token = authorizationHeader.replace("Bearer ", "");
        //todo
        //시간되면 유저 확인 하는 부분 refactor 하기...
        String authorization =null;

        Cookie[] cookies = request.getCookies();
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

            System.out.println("token null");
            //조건이 해당되면 메소드 종료한다.
            return ResponseEntity.status( HttpStatus.UNAUTHORIZED).body("Token null");
        }

        String token = authorization;
        //토큰
/*
        if(token.isEmpty()){
            return ResponseEntity.status( HttpStatus.UNAUTHORIZED).body("Token null"); //localhost:3000으로 리다이렉트 하기?
        }
*/
        // JWT 토큰에서 유저 이름 가져오기
        String username = jwtUtil.getUsername(token);
        if(username.isEmpty()){
            return ResponseEntity.status( HttpStatus.UNAUTHORIZED).body("User not found"); //유저를 찾을 수 없는 경우
        }
        //Cookie[] cookies=request.getCookies();


        UserEntity userEntity=userRepository.findByUsername( username );
        if(userEntity == null){
            return ResponseEntity.status( HttpStatus.UNAUTHORIZED).body("User not found"); //유저를 찾을 수 없는 경우
        }
        UserProfileDTO userProfile=userService.getUserProfile( userEntity );

        return ResponseEntity.ok( userProfile );
    }

    @GetMapping("/user/profile-temp")
    public ResponseEntity<?> userProfile(@RequestParam("id") Long userId){

        Optional<UserEntity> userEntity=userRepository.findById( userId );
        if(userEntity.isEmpty()){
            return ResponseEntity.status( HttpStatus.UNAUTHORIZED).body("User not found"); //유저를 찾을 수 없는 경우
        }
        UserProfileDTO userProfile=userService.getUserProfile( userEntity.get() );

        return ResponseEntity.ok( userProfile );
    }


    //유저 닉네임수정
    @PostMapping("/user/profile/edit")
    public ResponseEntity editUserNickname(HttpServletRequest request, @RequestParam("nickname") String newNickName){

        //String token = authorizationHeader.replace("Bearer ", "");

        String authorization =null;

        Cookie[] cookies = request.getCookies();
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

            System.out.println("token null");
            //조건이 해당되면 메소드 종료한다.
            return ResponseEntity.status( HttpStatus.UNAUTHORIZED).body("Token null");
        }

        String token = authorization;
        //토큰
/*
        if(token.isEmpty()){
            return ResponseEntity.status( HttpStatus.UNAUTHORIZED).body("Token null"); //localhost:3000으로 리다이렉트 하기?
        }
*/
        // JWT 토큰에서 유저 이름 가져오기
        String username = jwtUtil.getUsername(token);
        if(username.isEmpty()){
            return ResponseEntity.status( HttpStatus.UNAUTHORIZED).body("User not found"); //유저를 찾을 수 없는 경우
        }
        //Cookie[] cookies=request.getCookies();


        UserEntity userEntity=userRepository.findByUsername( username );
        if(userEntity == null){
            return ResponseEntity.status( HttpStatus.UNAUTHORIZED).body("User not found"); //유저를 찾을 수 없는 경우
        }

        userService.updateUserNickName(userEntity, newNickName);

        return ResponseEntity.ok("Updated user nickname");
    }

    @PostMapping("/user/profile/edit-temp")
    public ResponseEntity editUserNicknameTemp(@RequestParam("id") Long userId, @RequestParam("nickname") String newNickName){

        Optional<UserEntity> userEntity=userRepository.findById( userId );

        if(userEntity.isEmpty()){
            return ResponseEntity.status( HttpStatus.UNAUTHORIZED).body("User not found"); //유저를 찾을 수 없는 경우
        }

        userService.updateUserNickName(userEntity.get(), newNickName);

        return ResponseEntity.ok("Updated user nickname");
    }


}
