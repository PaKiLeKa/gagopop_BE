package pakirika.gagopop.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import pakirika.gagopop.service.GeoService;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class AdminAddressController {

    private final GeoService geoService;


    @PostMapping("/validate-address")
    @ResponseBody
    public Map<String, Object> validateAddress(@RequestBody Map<String, String> request) {
        String address = request.get("address");
        String jsonString = geoService.getKakaoApiFromAddress(address);

        Map<String, Object> response = new HashMap<>();
        try {
            geoService.changeToJSON(jsonString);
            response.put("valid", true);
        } catch (Exception e) {
            response.put("valid", false);
        }

        return response;
    }
}
