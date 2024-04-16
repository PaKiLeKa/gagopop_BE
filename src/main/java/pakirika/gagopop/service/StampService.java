package pakirika.gagopop.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pakirika.gagopop.dto.SimpleStampDto;
import pakirika.gagopop.dto.StampDTO;
import pakirika.gagopop.dto.TotalStampDTO;
import pakirika.gagopop.entity.*;
import pakirika.gagopop.repository.PopupStoreRepository;
import pakirika.gagopop.repository.StampRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StampService {

    private final StampRepository stampRepository;
    private final PopupStoreRepository popupStoreRepository;


    private final AmazonS3 s3;


    //TODO
    //스탬프 전체 조회

    public TotalStampDTO getAll(UserEntity user, LocalDate date){

        //유저 총 스탬프 수
        Long total=stampRepository.countByUserEntity( user );

        //유저 이번달 스탬프 수
        ZoneId koreaZone = ZoneId.of("Asia/Seoul");
        ZonedDateTime currentTimeInKorea = ZonedDateTime.now(koreaZone);
        LocalDate currentDateInKorea = currentTimeInKorea.toLocalDate();
        Long thisMonthStampTotal=stampRepository.countByUserAndMonth( user, currentDateInKorea );

        //유저 해당 달 스탬프 리스트
        List<Stamp> stampList=stampRepository.findByUserAndMonth( user, date);
        List<SimpleStampDto> simpleStampDtoList = new ArrayList<>();

        for(Stamp s: stampList){
            SimpleStampDto simpleStampDto = new SimpleStampDto();
            simpleStampDto.setStampId(s.getId());
            simpleStampDto.setPicture(s.getPicture());
            simpleStampDtoList.add(simpleStampDto);
        }

        TotalStampDTO totalStampDTO=new TotalStampDTO();
        totalStampDTO.setTotalStamp( total );
        totalStampDTO.setMonthlyStamp( thisMonthStampTotal );
        totalStampDTO.setStampList(simpleStampDtoList);

        return totalStampDTO;
    }





    @Transactional
    public boolean createUserStamp(UserEntity user, Long popupId, MultipartFile multipartFile, String date, String content, String withWho) throws IOException {

        Optional<PopupStore> popupStore=popupStoreRepository.findById( popupId );
        Optional<Stamp> existingUserStampList = stampRepository.findByUserEntityAndPopupStore(user, popupStore.get());

        //Todo
        //중복 에러 처리 안되는 문제 수정하기
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
            userStamp.setPopupStore(popupStore.get());
            userStamp.setPicture(fileUrl);
            userStamp.setDate(localDate);
            userStamp.setContent(content);
            userStamp.setWithWho(withWho);

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
    @Transactional
    public boolean deleteStamp(UserEntity user, Long popupId) throws IOException {

        Optional<PopupStore> popupStore=popupStoreRepository.findById( popupId );
        Optional<Stamp> existingUserStampList=stampRepository.findByUserEntityAndPopupStore( user, popupStore.get() );

        if (existingUserStampList.isPresent()) {
            String bucketName = "gagopop";
            String folderName = "user_stamp/";

            //todo
            //s3 버킷에서 이미지 삭제
            String pictureUrl=existingUserStampList.get().getPicture();

            String fileName = pictureUrl.substring(pictureUrl.lastIndexOf('/') + 1);

            String path= folderName+fileName;

            s3.deleteObject( bucketName, path );

            stampRepository.delete( existingUserStampList.get() );

            return true;
        }

        return false;
    }

    public StampDTO getDetail(UserEntity user, Long sid) {

        Optional<Stamp> stamp=stampRepository.findById( sid );

        Stamp userStamp = new Stamp();

        if(stamp.isPresent()) {
            userStamp=stamp.get();
            if (userStamp.getUserEntity().getId() == user.getId()) {
                StampDTO stampDTO=new StampDTO();
                stampDTO.setStampId( userStamp.getId() );
                stampDTO.setPopupId( userStamp.getPopupStore().getId() );
                stampDTO.setPopupName( userStamp.getPopupStore().getName() );
                stampDTO.setDate( userStamp.getDate() );
                stampDTO.setPicture( userStamp.getPicture() );
                stampDTO.setContent( userStamp.getContent() );
                stampDTO.setWithWho( userStamp.getWithWho() );

                return stampDTO;
            }
        }
        return null;
    }
}
