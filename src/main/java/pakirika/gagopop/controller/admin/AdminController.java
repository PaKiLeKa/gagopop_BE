package pakirika.gagopop.controller.admin;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pakirika.gagopop.repository.PopupStoreRepository;

@Controller
@RequiredArgsConstructor
public class AdminController {

    @GetMapping("/admin/login")
    public String login(){
        return "admin/login";
    }
    @GetMapping("/admin/dashboard")
    public String dashboard(){
        return "admin/dashboard";
    }

}
