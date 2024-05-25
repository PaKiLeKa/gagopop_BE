package pakirika.gagopop.controller.admin;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pakirika.gagopop.entity.PopupStore;
import pakirika.gagopop.repository.PopupStoreRepository;
import pakirika.gagopop.service.GeoService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminPopupStoreController {

    private final PopupStoreRepository popupStoreRepository;
    private final GeoService geoService;
    private final AmazonS3 s3;


    @GetMapping("/popup-stores")
    public String popupStores(Model model){
        List<PopupStore> popupStores = popupStoreRepository.findAllByOrderByIdDesc();
        model.addAttribute( "popupStores", popupStores );

        return "admin/popup-stores";
    }
    @GetMapping("/popup/add")
    public String addPopupStore(){
        return "admin/addForm";
    }

    //TODO
    //ModelAttribute로 수정 가능한지 확인해보고 리팩토링 하기
/*  @PostMapping(path = "/popup/add", consumes={MediaType.MULTIPART_FORM_DATA_VALUE})
    @Transactional
    public String addPopupStore(@RequestParam String name,
                                 @RequestParam String startDate,
                                 @RequestParam String endDate,
                                 @RequestParam String address,
                                 @RequestParam String operatingTime,
                                 @RequestParam String url,
                                 @RequestParam String info,
                                 @RequestPart MultipartFile img,
                                 @RequestParam(required=false) boolean isPromoted,
                                 @RequestParam(required = false) boolean isClosed,
                                 Model model) throws ParseException, net.minidev.json.parser.ParseException, IOException {

        PopupStore popupStore=new PopupStore();
        popupStore.setName( name );

        // startDate 파싱 및 조정
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern( "yyyy-MM-dd" );
        LocalDate startDateTime = LocalDate.parse(startDate, formatter);
        popupStore.setStartDate( startDateTime.atStartOfDay() );

        // endDate 파싱 및 조정
        LocalDate endDateTime = LocalDate.parse(endDate, formatter);
        popupStore.setEndDate(endDateTime.plusDays( 1 ).atStartOfDay().minusNanos( 1 ));

        popupStore.setAddress( address );

        String json=geoService.getKakaoApiFromAddress( address );
        ArrayList<Float> pos= geoService.changeToJSON( json );

        popupStore.setLongitude( pos.get( 0 ) );
        popupStore.setLatitude( pos.get( 1 ) );

        popupStore.setOperatingTime( operatingTime);

        if(isClosed==true){
            popupStore.setOpened( false  );
        }else {
            popupStore.setOpened( true );
        }

        popupStore.setInfo( info );

        String fileUrl= "";

        if(img.isEmpty()){
            fileUrl="https://kr.object.ncloudstorage.com/gagopop/null_image.png";
        }else {
            String bucketName = "gagopop";
            String folderName = "popup_img/";
            String fileNamePre = "popup";

            UUID uuid = UUID.randomUUID();
            String[] uuids = uuid.toString().split( "-");
            String uniqueName = uuids[0];

            String fileName = fileNamePre + "-" + uniqueName;
            String[] split = img.getOriginalFilename().split("\\.");
            String extension = split[split.length - 1];

            String newFileName = folderName +fileName+"."+extension;
            File file = convertMultiPartToFile(img);

            s3.putObject(new PutObjectRequest(bucketName, newFileName, file).withCannedAcl( CannedAccessControlList.PublicRead));

            fileUrl = s3.getUrl(bucketName, newFileName).toString();
            file.delete();
        }
        popupStore.setImageUrl( fileUrl );
        popupStore.setSnsLink( url );

        popupStore.setPromoted( isPromoted );

        popupStoreRepository.save( popupStore );

        return "redirect:/admin/popup-stores" ;
    }
*/

    @PostMapping(path = "/popup/add", consumes={MediaType.MULTIPART_FORM_DATA_VALUE})
    @Transactional
    public String addPopupStore(@ModelAttribute("popupStore") PopupStore popupStore,
                                  @RequestParam(required=false) boolean isPromoted,
                                  @RequestParam(required = false) boolean isClosed,
                                  @RequestPart MultipartFile img,
                                  Model model)
            throws ParseException, net.minidev.json.parser.ParseException, IOException {


        String json=geoService.getKakaoApiFromAddress( popupStore.getAddress() );
        ArrayList<Float> pos= geoService.changeToJSON( json );

        popupStore.setLongitude( pos.get( 0 ) );
        popupStore.setLatitude( pos.get( 1 ) );

        if(isClosed==true){
            popupStore.setOpened( false  );
        }else {
            popupStore.setOpened( true );
        }

        if(popupStore.getSnsLink() == null || popupStore.getSnsLink().trim().isEmpty()) {
            popupStore.setSnsLink("NULL");
        }

        popupStore.setPromoted( isPromoted );

        String fileUrl= "";
        try {
            String bucketName="gagopop";
            String folderName="popup_img/";
            String fileNamePre="popup";

            UUID uuid=UUID.randomUUID();
            String[] uuids=uuid.toString().split( "-" );
            String uniqueName=uuids[0];

            String fileName=fileNamePre + "-" + uniqueName;
            String[] split=img.getOriginalFilename().split( "\\." );
            String extension=split[split.length - 1];

            String newFileName=folderName + fileName + "." + extension;
            File file=convertMultiPartToFile( img );

            s3.putObject( new PutObjectRequest( bucketName, newFileName, file ).withCannedAcl( CannedAccessControlList.PublicRead ) );

            fileUrl=s3.getUrl( bucketName, newFileName ).toString();
            file.delete();
        }catch (FileNotFoundException e){
            fileUrl="https://kr.object.ncloudstorage.com/gagopop/null_image.png";
        }
        popupStore.setImageUrl( fileUrl );

        popupStoreRepository.save( popupStore );

        return "redirect:/admin/popup-stores" ;
    }


    @GetMapping("/popup/{popupId}")
    public String popup(@PathVariable Long popupId, Model model){
        Optional<PopupStore> optionalPopupStore=popupStoreRepository.findById( popupId );

        if (optionalPopupStore.isPresent()) {
            PopupStore popupStore = optionalPopupStore.get();


            LocalDate startDateNoTime = null;
            LocalDate endDateNoTime = null;
            // LocalDateTime에서 LocalDate로 변환
            if(popupStore.getStartDate() != null ){
                startDateNoTime = popupStore.getStartDate().toLocalDate();
            }
            if(popupStore.getEndDate() != null ){
                endDateNoTime = popupStore.getEndDate().toLocalDate();
            }

            model.addAttribute("popupStore", popupStore);
            model.addAttribute("startDateNoTime", startDateNoTime);
            model.addAttribute("endDateNoTime", endDateNoTime);
            model.addAttribute( "isClosed", !popupStore.isOpened() );

        } else {
            // Handle the case where the popup store is not found
            return "redirect:/admin/popup-stores"; // Redirect to a list or error page
        }
        return "admin/popup-store";
    }
    @GetMapping("/popup/{popupId}/edit")
    public String editPopup(@PathVariable Long popupId, Model model){
        Optional<PopupStore> optionalPopupStore=popupStoreRepository.findById( popupId );
        if (optionalPopupStore.isPresent()) {
            PopupStore popupStore = optionalPopupStore.get();

            // LocalDateTime에서 LocalDate로 변환

            LocalDate startDateNoTime = null;
            LocalDate endDateNoTime = null;
            // LocalDateTime에서 LocalDate로 변환
            if(popupStore.getStartDate() != null ){
                startDateNoTime = popupStore.getStartDate().toLocalDate();
            }
            if(popupStore.getEndDate() != null ){
                endDateNoTime = popupStore.getEndDate().toLocalDate();
            }

            model.addAttribute("popupStore", popupStore);
            model.addAttribute("startDateNoTime", startDateNoTime);
            model.addAttribute("endDateNoTime", endDateNoTime);
            model.addAttribute( "isClosed", !popupStore.isOpened() );
        } else {
            // Handle the case where the popup store is not found
            return "redirect:/admin/popup-stores"; // Redirect to a list or error page
        }

        return "admin/editForm";
    }

    @PostMapping(path = "/popup/{popupId}/edit", consumes={MediaType.MULTIPART_FORM_DATA_VALUE})
    @Transactional
    public String editPopup(@PathVariable Long popupId,
                            @ModelAttribute PopupStore popupStore,
                            @RequestPart MultipartFile img,
                            @RequestParam(required=false) boolean isPromoted,
                            @RequestParam(required = false) boolean isClosed
                            ) throws ParseException, net.minidev.json.parser.ParseException, IOException {

        Optional<PopupStore> optionalPopupStore=popupStoreRepository.findById( popupId );

        if (optionalPopupStore.isPresent()) {
            PopupStore exPopupStore=optionalPopupStore.get();

            exPopupStore.setName( popupStore.getName() );

            exPopupStore.setStartDate( popupStore.getStartDate() );
            exPopupStore.setEndDate( popupStore.getEndDate() );

            exPopupStore.setOperatingTime( popupStore.getOperatingTime() );
            exPopupStore.setInfo( popupStore.getInfo() );
            exPopupStore.setSnsLink( popupStore.getSnsLink() );

            //기존 주소와 다른 경우에만 주소로 위도 경도 다시 받아와서 저장
            if(!popupStore.getAddress().equals(exPopupStore.getAddress())){
                String json=geoService.getKakaoApiFromAddress( popupStore.getAddress() );
                ArrayList<Float> pos= geoService.changeToJSON( json );

                exPopupStore.setLongitude( pos.get( 0 ) );
                exPopupStore.setLatitude( pos.get( 1 ) );
            }

            if(isClosed==true){
                exPopupStore.setOpened( false  );
            }else {
                exPopupStore.setOpened( true );
            }

            exPopupStore.setPromoted(isPromoted);

            String fileUrl= "";

            String bucketName = "gagopop";
            String folderName = "popup_img/";
            String fileNamePre = "popup";

            try {

                String pictureUrl=exPopupStore.getImageUrl();

                //새 이미지 이름 설정
                UUID uuid = UUID.randomUUID();
                String[] uuids = uuid.toString().split( "-");
                String uniqueName = uuids[0];

                String fileName = fileNamePre + "-" + uniqueName;
                String[] split = img.getOriginalFilename().split("\\.");
                String extension = split[split.length - 1];

                String newFileName = folderName +fileName+"."+extension;
                File file = convertMultiPartToFile(img);

                //기존이미지가 NULL가 아닌 경우 삭제
                if(pictureUrl!="https://kr.object.ncloudstorage.com/gagopop/null_image.png"){
                    String existFileName = pictureUrl.substring(pictureUrl.lastIndexOf('/') + 1);
                    String path= folderName+existFileName;
                    s3.deleteObject( bucketName, path );
                }
                //아니라면 삭제 X하고 그냥 진행
                //새 이미지 저장
                s3.putObject(new PutObjectRequest(bucketName, newFileName, file).withCannedAcl( CannedAccessControlList.PublicRead));

                fileUrl = s3.getUrl(bucketName, newFileName).toString();
                exPopupStore.setImageUrl( fileUrl );
                file.delete();

            }
            catch (FileNotFoundException e){
                System.out.println("file not changed");
            }

            popupStoreRepository.save( exPopupStore );
            return "redirect:/admin/popup/{popupId}";
        }
        else {
            System.out.println("PopupStore not found");

            return "redirect:/admin/popup-stores";
        }

    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convertedFile=new File( file.getOriginalFilename() );
        FileOutputStream fos=new FileOutputStream( convertedFile );
        fos.write( file.getBytes() );
        fos.close();
        return convertedFile;
    }

    @GetMapping("/inquires")
    public String inquires(){
        return "admin/popup-stores";
    }
}
