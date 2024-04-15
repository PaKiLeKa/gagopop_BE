package pakirika.gagopop.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pakirika.gagopop.entity.*;
import pakirika.gagopop.repository.PopupStoreRepository;
import pakirika.gagopop.repository.StampRepository;
import pakirika.gagopop.repository.UserStampRepository;

import java.io.File;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserStampService {

    private final UserStampRepository userStampRepository;

    private final StampRepository stampRepository;

    private final PopupStoreRepository popupStoreRepository;

    private final AmazonS3Client amazonS3Client;


    //TODO
    //스템프 발급(게시글 등록)
    public boolean createUserStamp(UserEntity user, Long popupId, File file, LocalDate date, String content, String withWho) {

        Optional<PopupStore> popupStore=popupStoreRepository.findById( popupId );
        Optional<Stamp> stamp = stampRepository.findByPopupStore( popupStore.get() );
        Optional<UserStamp> existingUserStampList = userStampRepository.findByStampId(stamp.get().getId());

        if(existingUserStampList.isEmpty()){
            String bucketName = "gagopop";
            String folderName = "userStamp";
            String fileNamePre = "st-";

            //고유 이름 부여
            UUID uuid = UUID.randomUUID();
            String[] uuids = uuid.toString().split( "-" );
            String uniqueName = uuids[0];

            String fileName = fileNamePre + "-" + uuids;

            //Todo
            //Try catch로 변경하기
            amazonS3Client.putObject( new PutObjectRequest( bucketName, fileName, file ).withCannedAcl( CannedAccessControlList.PublicRead ) );
            String fileUrl=amazonS3Client.getUrl( bucketName, fileName ).toString();


            UserStamp userStamp = new UserStamp();

            userStamp.setUserEntity( user );
            userStamp.setStamp(stamp.get());
            userStamp.setPicture(fileUrl);
            userStamp.setContent(content);
            userStamp.setPicture(withWho);

            userStampRepository.save( userStamp );
            return true;
        }
        else {
            return false;
        }

    }






    //TODO
    //방문 인증 내용 수정



    //TODO
    //방문 인증 내용 삭제


}
