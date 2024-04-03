package pakirika.gagopop.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Lombok;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.HttpRequestHandler;
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


    @GetMapping("popup")
    public ResponseEntity<?> findByID(HttpServletRequest request, @RequestParam("pid") Long pid) {

        Optional<PopupStore> popupStore=popupStoreService.getPopupStore( pid );

        if(popupStore.isEmpty()){
            return ResponseEntity.status( HttpStatus.NOT_FOUND ).body( "PopupStore not found" );
        }
        //todo
        //유저 wishlist 정보도 같이 넣어줄 수 있도록 수정하기
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
        String username = null;
        if (authorization != null) { //헤더가 있으면
            String token = authorization; //가져오기
            // JWT 토큰에서 유저 이름 가져오기
            username = jwtUtil.getUsername(token); //토큰으로 유저이름 가져오기
        }
        UserEntity userEntity = null;
        if(username != null){ // null이 아니면 username으로 검색
            userEntity=userRepository.findByUsername( username );
        }


        //userEntity가 null이면 false 넣고 아니면  -> wishlist 찾아서 popupstore 있으면 같이넣기
        Optional<Wishlist> existingWishlist = wishlistRepository.findByUserEntityAndPopupStore(userEntity, popupStore.get());

        PopupWishDTO popupWishDTO=new PopupWishDTO();
        popupWishDTO.setPopupStore( popupStore.get() );

        if(existingWishlist.isEmpty()){
            popupWishDTO.setInWishlist( false );
        }else {
            popupWishDTO.setInWishlist( true );
        }

        return ResponseEntity.ok(popupWishDTO);
    }
    
    @GetMapping("popup/find")
    public List<PopupWishDTO> findPopupByNameOrAddress(HttpServletRequest request,
                                                              @RequestParam("name") String keyword,
                                                              @RequestParam("date") String dateString){

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate=LocalDate.parse( dateString, formatter );

            //List<PopupStore> result= popupStoreRepository.findOpenPopupStoresByNameDate( keyword, localDate );

            //TODO
            //여기서 가져온 리스트들을 오픈중/오픈예정/종료/종료임박으로 나눠서 드려야함
            //그리고 가능하면 몇개인지도 같이 보내주기
            //아니지 그냥 이름으로 검색할 때 애초에 나눠서가져오면됨

        //TODO
        //각각의 스토어가 유저의 위시리스트에 있는지도 같이 반환해야함
        //쿠키 보고 -> 유저 있는지 확인
        //          -> 유저가 있으면 -> 찾아서 true/false 넣기
        //          -->      없으면 -> 그냥 false 넣기
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
        String username = null;
        if (authorization != null) { //헤더가 있으면
            String token = authorization; //가져오기
            // JWT 토큰에서 유저 이름 가져오기
            username = jwtUtil.getUsername(token); //토큰으로 유저이름 가져오기
        }
        UserEntity userEntity = null;
        if(username != null){ // null이 아니면 username으로 검색
            userEntity=userRepository.findByUsername( username );
        }

        List<PopupStore> searchResult= popupStoreRepository.findOpenPopupStoresByNameDate( keyword, localDate );

        List<Long> popupInWishlist = new ArrayList<>();


        if (userEntity != null) {
            List<Wishlist> wishlists=wishlistRepository.findByUserEntity( userEntity );
            if (wishlists != null) {
                popupInWishlist=wishlists.stream()
                        .map( Wishlist::getPopupStore )
                        .filter( Objects::nonNull )
                        .map( PopupStore::getId )
                        .collect( Collectors.toList() );
            }
        }

        final List<Long> serchPopupStoreInWishlist = popupInWishlist;

        return searchResult.stream()
                .map(popupStore -> {
                    boolean isInWishlist = false;
                    if (serchPopupStoreInWishlist != null && serchPopupStoreInWishlist.contains(popupStore.getId())) {
                        isInWishlist = true;
                    }
                    return new PopupWishDTO(isInWishlist, popupStore );
                })
                .collect(Collectors.toList());

/*        List<PopupStore> openStores =popupStoreRepository.findOpenStoreByKeywordDate( keyword, localDate );
        popupStoreData.put( "open",openStores );
        List<PopupStore> scheduledStores =popupStoreRepository.findScheduledStoreByKeywordDate( keyword, localDate );
        popupStoreData.put( "will-Open",scheduledStores );
        List<PopupStore> closedStores =popupStoreRepository.findClosedStoreByKeywordDate( keyword, localDate );
        popupStoreData.put( "closed",closedStores );
        List<PopupStore> scheduledToCloseStores =popupStoreRepository.findScheduledToCloseStore( keyword, localDate );
        popupStoreData.put( "will-close",scheduledToCloseStores );*/


        //}
    }


    @GetMapping("popup/find-test")
    public ResponseEntity<?> findPopupByNameOrAddressWithDateTest(HttpServletRequest request,
                                                              @RequestParam("name") String keyword,
                                                              @RequestParam("date") String dateString){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(dateString, formatter);

        // Retrieve user information if available
        String authorization = null;
        String username = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie != null && (cookie.getName().equals("Authorization") || cookie.getName().equals("authorization"))) {
                    authorization = cookie.getValue();
                    break;
                }
            }
        }
        if (authorization != null) {
            String token = authorization;
            username = jwtUtil.getUsername(token);
        }

        UserEntity userEntity = null;
        if (username != null) {
            userEntity = userRepository.findByUsername(username);
        }

        // Map to store results
        Map<String, List<PopupWishDTO>> resultMap = new HashMap<>();

        // Retrieve lists of PopupStores
        List<PopupStore> openStores = popupStoreRepository.findOpenStoreByKeywordDate(keyword, localDate);
        List<PopupStore> scheduledStores = popupStoreRepository.findScheduledStoreByKeywordDate(keyword, localDate);
        List<PopupStore> closedStores = popupStoreRepository.findClosedStoreByKeywordDate(keyword, localDate);
        List<PopupStore> scheduledToCloseStores = popupStoreRepository.findScheduledToCloseStore(keyword, localDate);

        // Process each list of PopupStores
        resultMap.put("openStores", mapToPopupWishDTOList(openStores, userEntity));
        resultMap.put("scheduledStores", mapToPopupWishDTOList(scheduledStores, userEntity));
        resultMap.put("closedStores", mapToPopupWishDTOList(closedStores, userEntity));
        resultMap.put("scheduledToCloseStores", mapToPopupWishDTOList(scheduledToCloseStores, userEntity));

        return ResponseEntity.ok(resultMap);
    }


    private List<PopupWishDTO> mapToPopupWishDTOList(List<PopupStore> popupStores, UserEntity userEntity) {
        List<PopupWishDTO> popupWishDTOs = new ArrayList<>();
        for (PopupStore popupStore : popupStores) {
            PopupWishDTO popupWishDTO = new PopupWishDTO();
            popupWishDTO.setPopupStore(popupStore);
            popupWishDTO.setInWishlist(userEntity != null && isPopupStoreInWishlist(popupStore, userEntity));
            popupWishDTOs.add(popupWishDTO);
        }
        return popupWishDTOs;
    }

    private boolean isPopupStoreInWishlist(PopupStore popupStore, UserEntity userEntity) {
        List<Wishlist> wishlist = wishlistRepository.findByUserEntity(userEntity);
        for (Wishlist item : wishlist) {
            if (item.getPopupStore().equals(popupStore)) {
                return true;
            }
        }
        return false;
    }

    @GetMapping("popup/find-route")
    public List<PopupStore>  findPopupRouteByIdList(@RequestParam("pid") List<Long> pid,
                                                    @RequestParam("longitude") double longitude,
                                                    @RequestParam("latitude") double latitude){

        List<PopupStore> popupStores=popupStoreService.sortPopupStore(latitude, longitude, pid );

        return popupStores;
    }

    @GetMapping("popup/find-all")
    public List<PopupWishDTO> findPopupAllWithWish(HttpServletRequest request) {

        String authorization = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie != null && (cookie.getName().equals("Authorization") || cookie.getName().equals("authorization"))) {
                    authorization = cookie.getValue();
                    break;
                }
            }
        }
        String username = null;
        if (authorization != null) {
            String token = authorization;
            username = jwtUtil.getUsername(token);
        }

        UserEntity userEntity = null;
        if (username != null) {
            userEntity = userRepository.findByUsername(username);
        }

        List<PopupStore> allPopupStores = popupStoreRepository.findAll();

        List<Long> popupStoreIdsInWishlist = new ArrayList<>();


        if (userEntity != null) {
            List<Wishlist> wishlists=wishlistRepository.findByUserEntity( userEntity );
            if (wishlists != null) {
                popupStoreIdsInWishlist=wishlists.stream()
                        .map( Wishlist::getPopupStore )
                        .filter( Objects::nonNull )
                        .map( PopupStore::getId )
                        .collect( Collectors.toList() );
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

        Map<String, List<PopupStore>> popupStoreData = new LinkedHashMap<>();

        LocalDate today=this.getCurrentKoreaLocalDate(); //오늘 날짜 구하기 - 한국 기준


        List<PopupStore> fiveStores=popupStoreRepository.findFiveStores(today);
        popupStoreData.put("Header", fiveStores);

        //핫한 팝업 스토어 리스트 ( 현재 is_promoted 필드가 true인 걸로 임시 설정)
        List<PopupStore> hotStores=popupStoreRepository.findStoreIsPromoted(today);
        popupStoreData.put("hotStores", hotStores);

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
