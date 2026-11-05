package snc.app.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @GetMapping("/erreur/403")
    public String accesRefuse(Model model) {
        model.addAttribute("message", "Vous n'avez pas les droits nécessaires pour accéder à cette page.");
        return "error/403";
    }
}
