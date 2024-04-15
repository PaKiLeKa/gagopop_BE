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
import pakirika.gagopop.service.UserService;
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

    private final UserService userService;

    @GetMapping("/user/wishlist")
    public ResponseEntity<?> getUserWishlistAll(HttpServletRequest request){

        Optional<UserEntity> optionalUser=userService.findUser( request );
        if(optionalUser.isEmpty()){
            return ResponseEntity.status( HttpStatus.NOT_FOUND).body("User not found"); //유저를 찾을 수 없는 경우
        }
        List<WishTogoDTO> result = wishlistService.getPopupStoresInWishlistWithTogoByUserId(optionalUser.get().getId());

        return ResponseEntity.ok(result); //유저가 있으면 검색해서 보내기 (비어있는 경우 빈 객체 반환)
    }

    @GetMapping("/user/wishlist/add")
    public ResponseEntity<String> addToWishlist(HttpServletRequest request,
                                                @RequestParam("pid") Long popupStoreId) {
        Optional<UserEntity> optionalUser=userService.findUser( request );
        if(optionalUser.isEmpty()){
            return ResponseEntity.status( HttpStatus.NOT_FOUND).body("User not found"); //유저를 찾을 수 없는 경우
        }

        // popupStoreId를 이용하여 PopupStore 정보 가져오기
        Optional<PopupStore> popupStore = popupStoreRepository.findById(popupStoreId);
        if (popupStore == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Popup store not found");
        }
        // WishlistPopupStore에 데이터 추가
        boolean isDone=wishlistService.addToWishlist( optionalUser.get(), popupStore.get() );

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
        Optional<UserEntity> optionalUser=userService.findUser( request );
        if(optionalUser.isEmpty()){
            return ResponseEntity.status( HttpStatus.NOT_FOUND).body("User not found"); //유저를 찾을 수 없는 경우
        }

        // popupStoreId를 이용하여 PopupStore 정보 가져오기
        Optional<PopupStore> popupStore = popupStoreRepository.findById(popupStoreId);
        if (popupStore == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Popup store not found");
        }
        // WishlistPopupStore에 데이터 추가
        wishlistService.delete(optionalUser.get(), popupStore.get());
        return ResponseEntity.ok("Delete wishlist successfully");
    }
}
