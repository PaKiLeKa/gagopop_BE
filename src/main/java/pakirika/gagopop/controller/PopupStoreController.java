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
import java.util.*;
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
    public ResponseEntity<?> findPopupByNameAndDate(@RequestParam("name") String keyword,
                                                    @RequestParam("date") String dateString){


            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate=LocalDate.parse( dateString, formatter );

            //Todo
            //지역명 검색했을 때도 나오도록 수정해야함
            //두개의 리스트 얻어서, 중복 제거할 수 있도록? -> name or address 사용.
            List<PopupStore> result= popupStoreRepository.findOpenPopupStoresByNameDate( keyword, localDate );
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

    @GetMapping("popup/find-all-with-wish-temp")
    public List<PopupWishDTO> findPopupAllWithWishTemp(@RequestParam(name="id")Long userId) {
        List<PopupStore> allPopupStores = popupStoreRepository.findAll();

        List<Long> popupStoreIdsInWishlist = new ArrayList<>();


        Optional<UserEntity> userEntity=userRepository.findById( userId );

            if (userEntity.isPresent()) {
                List<Wishlist> wishlists = wishlistRepository.findByUserEntity(userEntity.get());
                if (wishlists != null) {
                    popupStoreIdsInWishlist = wishlists.stream()
                            .map(Wishlist::getPopupStore)
                            .filter( Objects::nonNull)
                            .map(PopupStore::getId)
                            .collect(Collectors.toList());
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

    @GetMapping("/popup/info")
    public Map<String, List<PopupStore>> popupInfo(){

        Map<String, List<PopupStore>> popupStoreData = new HashMap<>();

        LocalDate today=this.getCurrentKoreaLocalDate(); //오늘 날짜 구하기 - 한국 기준

        //핫한 팝업 스토어 리스트 ( 얜 기준을 뭐로 해야할까? )
        List<PopupStore> sixStores=popupStoreRepository.findSixStores(today);
        popupStoreData.put("hotStores", sixStores);

        //오픈 예정(오픈 날짜가 현재 날짜랑 가까운 순 -asc 쓰면되겠다)
        List<PopupStore> scheduledToOpen=popupStoreService.getPopupStoreScheduledToOpen( today );
        popupStoreData.put("scheduledToOpen", scheduledToOpen);


        //종료 임박( 종료 날짜가 현재 날짜랑 가까운 순 - 얘도 )
        List<PopupStore> scheduledToClose=popupStoreService.getPopupStoreScheduledToClose( today );
        popupStoreData.put("scheduledToClose", scheduledToClose);


        //넘길때 전체 각각 리스트객체로 해서 넘기기

        return popupStoreData;
    }

    public LocalDate getCurrentKoreaLocalDate() {
        // 현재 시스템의 기본 시간대로 Calendar 객체 생성
        Calendar calendar = Calendar.getInstance();

        // 기본 시간대를 한국 시간대로 설정
        TimeZone koreaTimeZone = TimeZone.getTimeZone("Asia/Seoul");
        calendar.setTimeZone(koreaTimeZone);

        // Calendar 객체에서 년도, 월, 일을 추출하여 LocalDate로 변환
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // 월은 0부터 시작하므로 1을 더해줌
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        return LocalDate.of(year, month, dayOfMonth);
    }
    
}
