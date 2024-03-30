package pakirika.gagopop.controller;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pakirika.gagopop.dto.WishTogoDTO;
import pakirika.gagopop.entity.PopupStore;
import pakirika.gagopop.entity.UserEntity;
import pakirika.gagopop.jwt.JWTUtil;
import pakirika.gagopop.repository.PopupStoreRepository;
import pakirika.gagopop.repository.UserRepository;
import pakirika.gagopop.service.WishlistService;

import java.util.*;


@RestController
@CrossOrigin
@RequiredArgsConstructor
public class WishlistController {

    private final UserRepository userRepository;
    private final PopupStoreRepository popupStoreRepository;
    private final WishlistService wishlistService;

    private final JWTUtil jwtUtil;

    @GetMapping("/user/wishlist")
    public ResponseEntity<?> getUserWishlistAll(HttpServletRequest request){

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

        List<WishTogoDTO> result = wishlistService.getPopupStoresInWishlistWithTogoByUserId(userEntity.getId());

        return ResponseEntity.ok(result); //유저가 있으면 검색해서 보내기 (비어있는 경우 빈 객체 반환)
    }


    @GetMapping("/user/wishlist/add")
    public ResponseEntity<String> addToWishlist(HttpServletRequest request,
                                                @RequestParam("pid") Long popupStoreId) {

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

        // JWT 토큰을 사용하여 사용자 정보 가져오기
        String username = jwtUtil.getUsername(token);

        if(username.isEmpty()){
            return ResponseEntity.status( HttpStatus.NOT_FOUND).body("User not found"); //유저를 찾을 수 없는 경우
        }

        UserEntity userEntity=userRepository.findByUsername( username );
        if(userEntity == null){
            return ResponseEntity.status( HttpStatus.NOT_FOUND).body("User not found"); //유저를 찾을 수 없는 경우
        }

        // popupStoreId를 이용하여 PopupStore 정보 가져오기
        Optional<PopupStore> popupStore = popupStoreRepository.findById(popupStoreId);
        if (popupStore == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Popup store not found");
        }
        // WishlistPopupStore에 데이터 추가
        boolean isDone=wishlistService.addToWishlist( userEntity, popupStore.get() );

        if(isDone){
            return ResponseEntity.ok("Added to wishlist successfully");
        }
        else {
            return ResponseEntity.ok("Already in Wishlist");
        }

    }




    @GetMapping("/user/wishlist/delete")
    public ResponseEntity<String> deleteToWishlist(HttpServletRequest request,
                                                @RequestParam("pid") Long popupStoreId) {

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

        // JWT 토큰을 사용하여 사용자 정보 가져오기
        String username = jwtUtil.getUsername(token);

        if(username.isEmpty()){
            return ResponseEntity.status( HttpStatus.NOT_FOUND).body("User not found"); //유저를 찾을 수 없는 경우
        }

        UserEntity userEntity=userRepository.findByUsername( username );
        if(userEntity == null){
            return ResponseEntity.status( HttpStatus.NOT_FOUND).body("User not found"); //유저를 찾을 수 없는 경우
        }

        // popupStoreId를 이용하여 PopupStore 정보 가져오기
        Optional<PopupStore> popupStore = popupStoreRepository.findById(popupStoreId);
        if (popupStore == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Popup store not found");
        }
        // WishlistPopupStore에 데이터 추가
        wishlistService.delete(userEntity, popupStore.get());
        return ResponseEntity.ok("Delete wishlist successfully");
    }
}
