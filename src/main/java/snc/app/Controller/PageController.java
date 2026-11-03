package snc.app.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    @GetMapping("/")
    public String accueil() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String pageConnexion() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String tableauDeBord() {
        return "dashboard";
    }
}
