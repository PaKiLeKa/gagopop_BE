package pakirika.gagopop.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import pakirika.gagopop.dto.StampDTO;
import pakirika.gagopop.dto.TotalStampDTO;
import pakirika.gagopop.entity.Stamp;
import pakirika.gagopop.entity.UserEntity;
import pakirika.gagopop.repository.UserRepository;
import pakirika.gagopop.service.UserService;
import pakirika.gagopop.service.StampService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class StampController {
    private final UserRepository userRepository;
    private final UserService userService;

    private final StampService stampService;

    private final Long testUserID = 1L;



    //TODO
    //중복 에러 처리!!!!
    @PostMapping(path = "/user/stamp/new", consumes={MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity newUserStamp(HttpServletRequest request,
                                       @RequestParam Long pid,
                                       @RequestParam String date,
                                       @RequestParam String content,
                                       @RequestParam String withWho,
                                       @RequestPart MultipartFile img) throws IOException {
        Optional<UserEntity> optionalUser=userService.findUser( request );

        if(optionalUser.isEmpty()){
            return ResponseEntity.status( HttpStatus.UNAUTHORIZED).body("User not found"); //유저를 찾을 수 없는 경우
        }



        boolean isCreated =stampService.createUserStamp( optionalUser.get(), pid, img, date, content, withWho );

        if(isCreated){
            return ResponseEntity.ok("Updated user stamp list");
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body( "Stamp already exist" );
        }

    }

    @PostMapping(path = "/user/stamp/new-test", consumes={MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity newUserStampTest(HttpServletRequest request,
                                       @RequestParam Long pid,
                                       @RequestParam String date,
                                       @RequestParam String content,
                                       @RequestParam String withWho,
                                       @RequestPart MultipartFile img) throws IOException {

        LocalDate localDate = LocalDate.parse( date );
        UserEntity testUser=userRepository.getById( testUserID );
        boolean isCreated =stampService.createUserStamp( testUser, pid, img, date, content, withWho );

        if(isCreated){
            return ResponseEntity.ok("Updated user stamp list");
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body( "Stamp already exist" );
        }
    }

    //수정
    @PostMapping(path = "/user/stamp/update", consumes={MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity updateUserStamp(HttpServletRequest request, @RequestParam StampDTO stamp, @RequestParam MultipartFile img) throws IOException {
        Optional<UserEntity> optionalUser = userService.findUser( request );

        if(optionalUser.isEmpty()){
            return ResponseEntity.status( HttpStatus.UNAUTHORIZED ).body( "Unauthorized" );
        }

        Stamp resultStamp=stampService.updateStamp( stamp, img );

        if(resultStamp == null){
            return ResponseEntity.status( HttpStatus.BAD_REQUEST).body( "Error: Stamp not updated" );
        }

        StampDTO stampDTO = stampService.getDetail( optionalUser.get(), resultStamp.getId() );

        return ResponseEntity.ok(stampDTO);
    }

    @PostMapping(path = "/user/stamp/update-test",  consumes={MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity updateUserStampTest(HttpServletRequest request, StampDTO stamp, @RequestParam MultipartFile img) throws IOException {
        UserEntity testUser=userRepository.getById(testUserID);

        Stamp resultStamp=stampService.updateStamp( stamp, img );

        if(resultStamp == null ){
            return ResponseEntity.status( HttpStatus.BAD_REQUEST).body( "Error: Stamp not updated" );
        }
        StampDTO stampDTO = stampService.getDetail( testUser, resultStamp.getId() );

        return ResponseEntity.ok(stampDTO);
    }

    //삭제
    @PostMapping("/user/stamp/delete")
    public ResponseEntity deleteUserStamp(HttpServletRequest request,
                                          @RequestParam Long pid) throws IOException {

        Optional<UserEntity> optionalUser=userService.findUser( request );

        if(optionalUser.isEmpty()){
            return ResponseEntity.status( HttpStatus.UNAUTHORIZED ).body( "Unauthorized" );
        }
        boolean isDeleted=stampService.deleteStamp( optionalUser.get(), pid );

        if(!isDeleted){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body( "Stamp not exist" );
        }

        return ResponseEntity.ok("Deleted user stamp list");
    }

    //삭제 테스트
    @PostMapping("/user/stamp/delete-test")
    public ResponseEntity deleteUserStampTest(HttpServletRequest request, @RequestParam Long pid) throws IOException {

        UserEntity testUser=userRepository.getById( testUserID );
        boolean isDeleted=stampService.deleteStamp( testUser, pid );

        if(!isDeleted){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body( "Stamp not exist" );
        }

        return ResponseEntity.ok("Deleted user stamp list");
    }


    //유저 스탬프 전체 레벨
    @GetMapping("/user/stamp/all")
    public ResponseEntity<?> showUserStampAll(HttpServletRequest request, @RequestParam("date") String dateString) throws IOException {
        Optional<UserEntity> optionalUser=userService.findUser( request );

        if(optionalUser.isEmpty()){
            return ResponseEntity.status( HttpStatus.UNAUTHORIZED ).body( "Unauthorized" );
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(dateString, formatter);

        TotalStampDTO totalStampDTO=stampService.getAll( optionalUser.get(), localDate );

        return ResponseEntity.ok(totalStampDTO);
    }

    //유저 스템프 전체 조회 테스트
    @GetMapping("/user/stamp/all-test")
    public ResponseEntity<?> showUserStampAllTest(HttpServletRequest request, @RequestParam("date") String dateString) throws IOException {
        UserEntity testUser=userRepository.getById( testUserID );

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(dateString, formatter);

        TotalStampDTO totalStampDTO=stampService.getAll(testUser , localDate );

        return ResponseEntity.ok(totalStampDTO);
    }

    //TODO
    //특정 스탬프 내역 조회
    @GetMapping("/user/stamp/show")
    public ResponseEntity<?> showUserStampDetail(HttpServletRequest request, @RequestParam("sid") Long sid){
        Optional<UserEntity> optionalUser=userService.findUser( request );

        if(optionalUser.isEmpty()){
            return ResponseEntity.status( HttpStatus.UNAUTHORIZED ).body( "Unauthorized" );
        }

        StampDTO stampDTO=stampService.getDetail( optionalUser.get(), sid );

        if(stampDTO == null){
            return ResponseEntity.status( HttpStatus.NOT_FOUND ).body( "Stamp not found" );
        }

        return ResponseEntity.ok(stampDTO);
    }

    @GetMapping("/user/stamp/show-test")
    public ResponseEntity<?> showUserStampDetailTest(HttpServletRequest request, @RequestParam("sid") Long sid){
        UserEntity testUser=userRepository.getById( testUserID );

        StampDTO stampDTO=stampService.getDetail( testUser, sid );

        if(stampDTO == null){
            return ResponseEntity.status( HttpStatus.NOT_FOUND ).body( "Stamp not found" );
        }

        return ResponseEntity.ok(stampDTO);
    }



}
