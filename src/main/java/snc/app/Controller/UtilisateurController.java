package snc.app.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import snc.app.Repository.UtilisateurRepository;
import snc.app.Services.UtilisateurService;

@Controller
@RequestMapping("/utilisateurs")
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    public UtilisateurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @GetMapping("/")
    public String LoginPage(){
        return "redirect:/login";
    }

    @PostMapping("/login")
    public String Login(@RequestParam String username, @RequestParam String password, Model model){
        
        if(username.isEmpty() || password.isEmpty()){
            model.addAttribute("error", "error_empty");
            return "redirect:/login";
        }
        if (!utilisateurService.IsUserValid(username, password)) {
            model.addAttribute("error", "error_authentification");
            return "redirect:/login";
        }
        return "redirect:/dashboard";

    }
}
