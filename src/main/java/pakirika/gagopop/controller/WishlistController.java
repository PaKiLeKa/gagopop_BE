package pakirika.gagopop.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pakirika.gagopop.entity.PopupStore;
import pakirika.gagopop.entity.UserEntity;
import pakirika.gagopop.jwt.JWTUtil;
import pakirika.gagopop.repository.UserRepository;
import pakirika.gagopop.service.WishlistService;

import java.util.*;


@RestController
@CrossOrigin
@RequiredArgsConstructor
public class WishlistController {

    private final UserRepository userRepository;
    private final WishlistService wishlistService;

    private final JWTUtil jwtUtil;

    @GetMapping("/user/wishlist")
    public ResponseEntity<?> getUserWishlistAll(@RequestHeader("Authorization") String authorizationHeader){

        // Authorization 헤더에서 JWT 토큰 추출하기!
        String token = authorizationHeader.replace("Bearer ", "");

        // JWT 토큰에서 유저 이름 가져오기
        String username = jwtUtil.getUsername(token);

        UserEntity userEntity=userRepository.findByUsername( username );
//        if(userEntity.isEmpty()){
//            return ResponseEntity.status( HttpStatus.NOT_FOUND).body("해당 사용자를 찾을 수 없습니다."); //유저를 찾을 수 없는 경우
//        }
        //토큰 만료된 경우 예외처리 필요
        List<PopupStore> allPopupStoresInUserWishlist=wishlistService.getAllPopupStoresInUserWishlist( userEntity );

        return ResponseEntity.ok(allPopupStoresInUserWishlist); //유저가 있으면 검색해서 보내기 (비어있는 경우 빈 객체 반환)
    }


}
