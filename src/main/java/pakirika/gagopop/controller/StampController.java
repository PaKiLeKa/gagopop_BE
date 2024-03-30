package pakirika.gagopop.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pakirika.gagopop.entity.Stamp;
import pakirika.gagopop.entity.UserEntity;
import pakirika.gagopop.repository.UserRepository;
import pakirika.gagopop.service.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class StampController {
    private final UserRepository userRepository;
    private final UserService userService;

    //TODO
    //유저 엔티티를 찾아서 -> 유저로 stamplist 검색 -> 해당 유저가 가지고있는 stamp들 list로 만들기
    @GetMapping("/user/stamp-list")
    public ResponseEntity<?> getUserStampList(@RequestParam("id") Long userId){
        Optional<UserEntity> userEntity=userRepository.findById( userId );

        if(userEntity.isEmpty()){
            //status( HttpStatus.UNAUTHORIZED).body("User not found");
        }

        List<Stamp> usersStampAll=userService.findUsersStampAll( userEntity.get() );
        return ResponseEntity.ok(usersStampAll);

    }

}
