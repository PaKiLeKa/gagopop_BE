package pakirika.gagopop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pakirika.gagopop.entity.PopupStore;
import pakirika.gagopop.service.PopupStoreService;
import pakirika.gagopop.repository.PopupStoreRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class PopupStoreController {

    private final PopupStoreRepository popupStoreRepository;
    private final PopupStoreService popupStoreService;
    
    @GetMapping("popup/find")
    public List<PopupStore>  findPopupByNameAndDate(@RequestParam("name") String name, @RequestParam("date") String dateString){


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate=LocalDate.parse( dateString, formatter );
        //Date date=Date.from( localDate.atStartOfDay( ZoneId.systemDefault() ).toInstant() );
        List<PopupStore> result= popupStoreRepository.findOpenPopupStoresByNameDate( name, localDate );

        return result;
    }

    @GetMapping("popup/route/find")
    public List<PopupStore>  findPopupRouteByIdList(@RequestParam("pid") List<Long> pid,
                                                    @RequestParam("longitude") double longitude,
                                                    @RequestParam("latitude") double latitude){

        List<PopupStore> popupStores=popupStoreService.sortPopupStore(latitude, longitude, pid );

        return popupStores;
    }

    @GetMapping("popup/findAll")
    public List<PopupStore> findPopupAll(){
        List<PopupStore> allPopupStores = popupStoreRepository.findAll();
        // TODO
        // 서비스가 커진 후에 날짜 제한 없이 보내면 데이터가 너무 클 것 같다.
        // 일단 데이터가 많이 없으니 구현부터 해보고 추후 날짜 범위 논의 해보기

        return allPopupStores;
    }

    
}
