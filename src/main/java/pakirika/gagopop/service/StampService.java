package pakirika.gagopop.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pakirika.gagopop.entity.*;
import pakirika.gagopop.repository.PopupStoreRepository;
import pakirika.gagopop.repository.StampRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StampService {

    private final StampRepository stampRepository;
    private final PopupStoreRepository popupStoreRepository;


    private final AmazonS3 s3;

    //TODO
    //스템프 발급(게시글 등록)
    public boolean createUserStamp(UserEntity user, Long popupId, MultipartFile multipartFile, String date, String content, String withWho) throws IOException {

        Optional<PopupStore> popupStore=popupStoreRepository.findById( popupId );
        Optional<Stamp> existingUserStampList = stampRepository.findByUserEntityAndPopupStore(user, popupStore.get());

        if(existingUserStampList.isEmpty()){
            String bucketName = "gagopop";
            String folderName = "user_stamp/";
            String fileNamePre = "st";

            //고유 이름 부여
            UUID uuid = UUID.randomUUID();
            String[] uuids = uuid.toString().split( "-");
            String uniqueName = uuids[0];

            String fileName = fileNamePre + "-" + uniqueName;
            String[] split = multipartFile.getOriginalFilename().split("\\.");
            String extension = split[split.length - 1];
            //System.out.println(extension);

            String newFileName = folderName +fileName+"."+extension;

            File file = convertMultiPartToFile(multipartFile);

            //Todo
            //Try catch로 변경하기
            s3.putObject(new PutObjectRequest(bucketName, newFileName, file).withCannedAcl(CannedAccessControlList.PublicRead));

            String fileUrl=s3.getUrl(bucketName, newFileName).toString();

            LocalDate localDate = LocalDate.parse(date);
            Stamp userStamp = new Stamp();

            userStamp.setUserEntity(user);
            userStamp.setPopupStore( popupStore.get() );
            userStamp.setPicture(fileUrl);
            userStamp.setDate(localDate.atStartOfDay());
            userStamp.setContent(content);
            userStamp.setPicture(withWho);

            stampRepository.save( userStamp );
            return true;
        }
        else {
            return false;
        }
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convertedFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convertedFile);
        fos.write(file.getBytes());
        fos.close();
        return convertedFile;
    }


    //TODO
    //방문 인증 내용 수정


    //TODO
    //방문 인증 내용 삭제


}
