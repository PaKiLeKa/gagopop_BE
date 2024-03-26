package pakirika.gagopop.popupStore;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class PopupStoreController {

    private final PopupStoreRepository popupStoreRepository;
    private final PopupStoreService popupStoreService;
    
    @GetMapping("/api/find")
    public List<PopupStore>  findPopupByNameAndDate(@RequestParam("name") String name, @RequestParam("date") String dateString){


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate=LocalDate.parse( dateString, formatter );
        //Date date=Date.from( localDate.atStartOfDay( ZoneId.systemDefault() ).toInstant() );
        List<PopupStore> result= popupStoreRepository.findOpenPopupStoresByNameDate( name, localDate );

        return result;
    }
    
    
}
