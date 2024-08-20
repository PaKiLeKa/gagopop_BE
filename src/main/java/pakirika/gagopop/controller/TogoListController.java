package pakirika.gagopop.controller;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pakirika.gagopop.dto.TogoListDTO;
import pakirika.gagopop.entity.PopupStore;
import pakirika.gagopop.entity.TogoList;
import pakirika.gagopop.entity.UserEntity;
import pakirika.gagopop.repository.UserRepository;
import pakirika.gagopop.service.PopupStoreService;
import pakirika.gagopop.service.TogoListService;
import pakirika.gagopop.service.UserService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@CrossOrigin
public class TogoListController {

    private final TogoListService togoListService;
    private final PopupStoreService popupStoreService;
    private final UserService userService;
    private final UserRepository userRepository;

    private final Long testUserID=1L;

    //생성 test
    @PostMapping("/togo/create-test")
    public ResponseEntity createTogoListTest(@RequestParam("name") String togoName){

        UserEntity testUser=userRepository.getById( testUserID );

        TogoList togoList=togoListService.createTogoList( testUser, togoName );

        return ResponseEntity.ok("Togo list is created successfully");

    }
    @PostMapping("/togo/create")
    public ResponseEntity createTogoList(HttpServletRequest request, @RequestParam("name") String togoName){

        Optional<UserEntity> optionalUser = userService.findUser( request );

        if(optionalUser.isEmpty()){
            return ResponseEntity.status( HttpStatus.UNAUTHORIZED ).body( "Unauthorized" );
        }

        TogoList togoList=togoListService.createTogoList( optionalUser.get(), togoName );

        return ResponseEntity.ok(togoList);

    }

    //투고 id로 개별 조회
    @GetMapping("/togo/show")
    public ResponseEntity showTogoList(HttpServletRequest request, @RequestParam("tid") Long togoId){
        Optional<UserEntity> optionalUser = userService.findUser( request );

        if(optionalUser.isEmpty()){
            return ResponseEntity.status( HttpStatus.UNAUTHORIZED ).body( "Unauthorized" );
        }

        Optional<TogoList> optionalTogoList=togoListService.getTogoList( optionalUser.get(), togoId );

        if(optionalTogoList.isEmpty()){
            return ResponseEntity.status( HttpStatus.NOT_FOUND ).body("Togo list not found");
        }

        return ResponseEntity.ok(optionalTogoList.get());
    }

    @GetMapping("/togo/show-test")
    public ResponseEntity showTogoListTest(HttpServletRequest request, @RequestParam("tid") Long togoId){
        UserEntity testUser=userRepository.getById( testUserID );

        Optional<TogoList> optionalTogoList=togoListService.getTogoList( testUser, togoId );

        if(optionalTogoList.isEmpty()){
            return ResponseEntity.status( HttpStatus.NOT_FOUND ).body("Togo list not found");
        }

        return ResponseEntity.ok(optionalTogoList.get());
    }

    //User의 전체 togo 조회
    @GetMapping("/togo/all")
    public ResponseEntity showTogoListAll(HttpServletRequest request){
        Optional<UserEntity> optionalUser = userService.findUser( request );

        if(optionalUser.isEmpty()){
            return ResponseEntity.status( HttpStatus.UNAUTHORIZED ).body( "Unauthorized" );
        }

        List<TogoList> togoLists = togoListService.getAllTogoLists(optionalUser.get());

        if (togoLists == null || togoLists.isEmpty()) {
            // TogoList가 null이거나 비어 있는 경우
            return ResponseEntity.ok("No Togo Lists found for this user."); // 빈 리스트 메시지 반환
        }

        List<TogoListDTO> dtoList = togoLists.stream()
                .map(togoListService::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtoList);


    }

    @GetMapping("/togo/all-test")
    public ResponseEntity showTogoListAllTest(HttpServletRequest request){
        UserEntity testUser=userRepository.getById( testUserID );

        List<TogoList> togoLists = togoListService.getAllTogoLists(testUser);

        if (togoLists == null || togoLists.isEmpty()) {
            // TogoList가 null이거나 비어 있는 경우
            return ResponseEntity.ok("No Togo Lists found for this user."); // 빈 리스트 메시지 반환
        }

        List<TogoListDTO> dtoList = togoLists.stream()
                .map(togoListService::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtoList);
    }


    //todo
    //팝업 추가
    @PostMapping("/togo/popup/add")
    public ResponseEntity addPopupToTogoList(HttpServletRequest request,
                                  @RequestParam("TogoId") Long togoId,
                                  @RequestParam("popupId") Long pid ){

        Optional<UserEntity> optionalUser = userService.findUser( request );

        if(optionalUser.isEmpty()){
            return ResponseEntity.status( HttpStatus.UNAUTHORIZED ).body( "Unauthorized" );
        }

       UserEntity userEntity=optionalUser.get();

       Optional<PopupStore> optionalPopupStore=popupStoreService.getPopupStore( pid );

       if(optionalPopupStore.isEmpty()){
           return ResponseEntity.status( HttpStatus.NOT_FOUND ).body( "Popup Store Not Found" );
       }

       //boolean b = togoListService.addPopupStore( togoId, optionalUser.get(), optionalPopupStore.get() );
       boolean b = togoListService.addPopupStore( togoId, userEntity, optionalPopupStore.get() );

       if(!b){
           return ResponseEntity.status( HttpStatus.BAD_REQUEST ).body( "Failed to add Popup Store" );
       }

       return ResponseEntity.ok("Add popup store successfully");

   }

    @PostMapping("/togo/popup/add-test")
    public ResponseEntity addPopupToTogoListTest(HttpServletRequest request,
                                            @RequestParam("TogoId") Long togoId,
                                            @RequestParam("popupId") Long pid ){


        UserEntity testUser=userRepository.getById( testUserID );

        Optional<PopupStore> optionalPopupStore=popupStoreService.getPopupStore( pid );

        if(optionalPopupStore.isEmpty()){
            return ResponseEntity.status( HttpStatus.NOT_FOUND ).body( "Popup Store Not Found" );
        }

        //boolean b = togoListService.addPopupStore( togoId, optionalUser.get(), optionalPopupStore.get() );
        boolean b = togoListService.addPopupStore( togoId, testUser, optionalPopupStore.get() );

        if(!b){
            return ResponseEntity.status( HttpStatus.BAD_REQUEST ).body( "Failed to add Popup Store" );
        }

        return ResponseEntity.ok("Add popup store successfully");

    }


    @PostMapping("/togo/popup/remove-test")
    public ResponseEntity removePopupToTogoListTest(HttpServletRequest request,
                                                @RequestParam("TogoId") Long togoId,
                                                @RequestParam("popupId") Long pid ){

        Optional<UserEntity> optionalUser = userService.findUser( request );

        if(optionalUser.isEmpty()){
            return ResponseEntity.status( HttpStatus.UNAUTHORIZED ).body( "Unauthorized" );
        }

        UserEntity userEntity=optionalUser.get();

        Optional<PopupStore> optionalPopupStore=popupStoreService.getPopupStore( pid );

        if(optionalPopupStore.isEmpty()){
            return ResponseEntity.status( HttpStatus.NOT_FOUND ).body( "Popup Store Not Found" );
        }

        //boolean b = togoListService.addPopupStore( togoId, optionalUser.get(), optionalPopupStore.get() );
        boolean b = togoListService.removePopupStore( togoId, userEntity, optionalPopupStore.get() );

        if(!b){
            return ResponseEntity.status( HttpStatus.BAD_REQUEST ).body( "Failed to remove Popup Store" );
        }

        return ResponseEntity.ok("Remove popup store successfully");

    }

    @PostMapping("/togo/popup/remove")
    public ResponseEntity removePopupToTogoList(HttpServletRequest request,
                                                    @RequestParam("TogoId") Long togoId,
                                                    @RequestParam("popupId") Long pid ){

        UserEntity testUser=userRepository.getById( testUserID );

        Optional<PopupStore> optionalPopupStore=popupStoreService.getPopupStore( pid );

        if(optionalPopupStore.isEmpty()){
            return ResponseEntity.status( HttpStatus.NOT_FOUND ).body( "Popup Store Not Found" );
        }

        //boolean b = togoListService.addPopupStore( togoId, optionalUser.get(), optionalPopupStore.get() );
        boolean b = togoListService.removePopupStore( togoId, testUser, optionalPopupStore.get() );

        if(!b){
            return ResponseEntity.status( HttpStatus.BAD_REQUEST ).body( "Failed to remove Popup Store" );
        }

        return ResponseEntity.ok("Remove popup store successfully");

    }


    //투고 삭제
    @GetMapping("/togo/delete")
    public ResponseEntity<String> deleteToGoList(HttpServletRequest request,
                                                   @RequestParam("id") Long togoId) {
        Optional<UserEntity> optionalUser = userService.findUser( request );
        if(optionalUser.isEmpty()){
            return ResponseEntity.status( HttpStatus.UNAUTHORIZED).body("User not found"); //유저를 찾을 수 없는 경우
        }

        // popupStoreId를 이용하여 PopupStore 정보 가져오기
        // WishlistPopupStore에 데이터 추가
        Optional<TogoList> optionalTogoList = togoListService.getTogoList( optionalUser.get(), togoId );
        if (optionalTogoList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Togo List not found");
        }
        togoListService.deleteTogoList(optionalUser.get(), optionalTogoList.get());
        return ResponseEntity.ok("Delete Togo list successfully");
    }

    @GetMapping("/togo/delete-test")
    public ResponseEntity<String> deleteToGoListTest(HttpServletRequest request,
                                                 @RequestParam("id") Long togoId) {
        UserEntity testUser=userRepository.getById( testUserID );

        // popupStoreId를 이용하여 PopupStore 정보 가져오기
        // WishlistPopupStore에 데이터 추가
        Optional<TogoList> optionalTogoList = togoListService.getTogoList( testUser, togoId );
        if (optionalTogoList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Togo List not found");
        }
        togoListService.deleteTogoList(testUser, optionalTogoList.get());
        return ResponseEntity.ok("Delete Togo list successfully");
    }
}

