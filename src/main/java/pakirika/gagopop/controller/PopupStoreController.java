package pakirika.gagopop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pakirika.gagopop.dto.PopupWishDTO;
import pakirika.gagopop.entity.PopupStore;
import pakirika.gagopop.entity.Wishlist;
import pakirika.gagopop.entity.UserEntity;
import pakirika.gagopop.jwt.JWTUtil;
import pakirika.gagopop.repository.UserRepository;
import pakirika.gagopop.repository.WishlistRepository;
import pakirika.gagopop.service.PopupStoreService;
import pakirika.gagopop.repository.PopupStoreRepository;
import pakirika.gagopop.service.WishlistService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class PopupStoreController {

    private final PopupStoreRepository popupStoreRepository;
    private final PopupStoreService popupStoreService;

    private final UserRepository userRepository;

    private final WishlistRepository wishlistRepository;

    private final WishlistService wishlistService;

    private final JWTUtil jwtUtil;
    
    @GetMapping("popup/find")
    public ResponseEntity<?> findPopupByNameAndDate(@RequestParam("name") String name,
                                                    @RequestParam("date") String dateString){


            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate=LocalDate.parse( dateString, formatter );
            //Date date=Date.from( localDate.atStartOfDay( ZoneId.systemDefault() ).toInstant() );
            List<PopupStore> result= popupStoreRepository.findOpenPopupStoresByNameDate( name, localDate );
            return ResponseEntity.ok(result);
        //}
    }

    @GetMapping("popup/find-route")
    public List<PopupStore>  findPopupRouteByIdList(@RequestParam("pid") List<Long> pid,
                                                    @RequestParam("longitude") double longitude,
                                                    @RequestParam("latitude") double latitude){

        List<PopupStore> popupStores=popupStoreService.sortPopupStore(latitude, longitude, pid );

        return popupStores;
    }

    @GetMapping("popup/find-all")
    public List<PopupStore> findPopupAll(@RequestHeader(value="Authorization", required=false) String authorizationHeader){


        List<PopupStore> allPopupStores = popupStoreRepository.findAll();
        // TODO
        // 서비스가 커진 후에 날짜 제한 없이 보내면 데이터가 너무 클 것 같다.
        // 일단 데이터가 많이 없으니 구현부터 해보고 추후 날짜 범위 논의 해보기

        return allPopupStores;
    }

    @GetMapping("popup/find-all-with-wish")
    public List<PopupWishDTO> findPopupAllWithWish(@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        List<PopupStore> allPopupStores = popupStoreRepository.findAll();

        List<Long> popupStoreIdsInWishlist = new ArrayList<>();

        if (authorizationHeader != null) {
            UserEntity userEntity = getUserIdFromAuthorizationHeader(authorizationHeader);
            if (userEntity != null) {
                List<Wishlist> wishlists = wishlistRepository.findByUserEntity(userEntity);
                if (wishlists != null) {
                    popupStoreIdsInWishlist = wishlists.stream()
                            .map(Wishlist::getPopupStore)
                            .filter( Objects::nonNull)
                            .map(PopupStore::getId)
                            .collect(Collectors.toList());
                }
            }
        }

        final List<Long> finalPopupStoreIdsInWishlist = popupStoreIdsInWishlist;

        return allPopupStores.stream()
                .map(popupStore -> {
                    boolean isInWishlist = false;
                    if (finalPopupStoreIdsInWishlist != null && finalPopupStoreIdsInWishlist.contains(popupStore.getId())) {
                        isInWishlist = true;
                    }
                    return new PopupWishDTO(isInWishlist, popupStore );
                })
                .collect(Collectors.toList());
    }



    // Assuming you have a method to extract userId from the authorizationHeader
    private UserEntity getUserIdFromAuthorizationHeader(String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");

        if(token.isEmpty()){
          return null;
        }
        // JWT 토큰에서 유저 이름 가져오기
        String username = jwtUtil.getUsername(token);
        if(username.isEmpty()){
            return null;
        }

        UserEntity userEntity=userRepository.findByUsername( username );
        if(userEntity == null){
            return null;
        }


        return userEntity;
    }
    
}
