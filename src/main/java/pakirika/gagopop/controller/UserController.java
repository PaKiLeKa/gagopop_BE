package pakirika.gagopop.controller;

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

@RestController
@RequiredArgsConstructor
@Transactional
public class UserController {

    private final UserService userService;
    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;

    //회원 전체 정보 -username, email, stamp 총 개수, wishlist 총 개수, togo list 총 개수

    @GetMapping("/user/profile")
    public ResponseEntity<?> userProfile(@RequestHeader("Authorization") String authorizationHeader){
        String token = authorizationHeader.replace("Bearer ", "");

        if(token.isEmpty()){
            return ResponseEntity.status( HttpStatus.UNAUTHORIZED).body("Token null"); //localhost:3000으로 리다이렉트 하기?
        }
        // JWT 토큰에서 유저 이름 가져오기
        String username = jwtUtil.getUsername(token);
        if(username.isEmpty()){
            return ResponseEntity.status( HttpStatus.UNAUTHORIZED).body("User not found"); //유저를 찾을 수 없는 경우
        }

        UserEntity userEntity=userRepository.findByUsername( username );
        if(userEntity == null){
            return ResponseEntity.status( HttpStatus.UNAUTHORIZED).body("User not found"); //유저를 찾을 수 없는 경우
        }
        UserProfileDTO userProfile=userService.getUserProfile( userEntity );

        return ResponseEntity.ok( userProfile );
    }


    //유저 닉네임수정
    @PostMapping("/user/profile/edit")
    public ResponseEntity editUserNickname(@RequestHeader("Authorization") String authorizationHeader, @RequestParam("nickname") String newNickName){
        String token = authorizationHeader.replace("Bearer ", "");

        if(token.isEmpty()){
            return ResponseEntity.status( HttpStatus.UNAUTHORIZED).body("Token null"); //localhost:3000으로 리다이렉트 하기?
        }
        // JWT 토큰에서 유저 이름 가져오기
        String username = jwtUtil.getUsername(token);
        if(username.isEmpty()){
            return ResponseEntity.status( HttpStatus.UNAUTHORIZED).body("User not found"); //유저를 찾을 수 없는 경우
        }

        UserEntity userEntity=userRepository.findByUsername( username );
        if(userEntity == null){
            return ResponseEntity.status( HttpStatus.UNAUTHORIZED).body("User not found"); //유저를 찾을 수 없는 경우
        }

        userService.updateUserNickName(userEntity, newNickName);

        return ResponseEntity.ok("Updated user nickname");
    }




}
