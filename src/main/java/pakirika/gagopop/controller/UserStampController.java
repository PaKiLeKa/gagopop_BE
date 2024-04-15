package pakirika.gagopop.controller;


import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pakirika.gagopop.entity.Stamp;
import pakirika.gagopop.entity.UserEntity;
import pakirika.gagopop.repository.UserRepository;
import pakirika.gagopop.service.UserService;
import pakirika.gagopop.service.UserStampService;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UserStampController {
    private final UserRepository userRepository;
    private final UserService userService;

    private final UserStampService userStampService;

    private final AmazonS3Client amazonS3Client;



    //TODO
    //유저 엔티티를 찾아서 -> 유저로 stamplist 검색 -> 해당 유저가 가지고있는 stamp들 list로 만들기
    @GetMapping("/user/stamp/list")
    public ResponseEntity<?> getUserStampList(@RequestParam("id") Long userId){
        Optional<UserEntity> userEntity=userRepository.findById( userId );

        if(userEntity.isEmpty()){
            //status( HttpStatus.UNAUTHORIZED).body("User not found");
        }

        List<Stamp> usersStampAll=userService.findUsersStampAll( userEntity.get() );
        return ResponseEntity.ok(usersStampAll);

    }

    //TODO
    //스템프 발급(게시글 등록)
    @PostMapping("/user/stamp/new")
    public ResponseEntity newUserStamp(HttpServletRequest request, @RequestParam("file") File file){
        Optional<UserEntity> optionalUser=userService.findUser( request );
        if(optionalUser.isEmpty()){
            return ResponseEntity.status( HttpStatus.UNAUTHORIZED).body("User not found"); //유저를 찾을 수 없는 경우
        }

        Long popupID = Long.valueOf( request.getParameter( "pid" ) );
        LocalDate date = LocalDate.parse( request.getParameter( "date") );
        String content = request.getParameter( "content" );
        String withWho = request.getParameter( "withWho" );

        boolean isCreated =userStampService.createUserStamp( optionalUser.get(), popupID, file, date, content, withWho );

        if(isCreated){
            return ResponseEntity.ok("Updated user stamp list");
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body( "Stamp already exist" );
        }

    }


    //TODO
    //방문 인증 내용 수정
    @PostMapping("/user/stamp/update")
    public ResponseEntity updateUserStamp(HttpServletRequest request){

        return ResponseEntity.ok("Updated user stamp list");
    }


    //TODO
    //방문 인증 내용 삭제
    @PostMapping("/user/stamp/delete")
    public ResponseEntity deleteUserStamp(HttpServletRequest request){

        return ResponseEntity.ok("Updated user stamp list");

    }

}
