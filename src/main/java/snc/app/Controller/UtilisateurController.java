package snc.app.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import snc.app.Model.Utilisateur;
import snc.app.Repository.UtilisateurRepository;
import snc.app.Services.UtilisateurService;

import java.util.List;

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

    ///////////////////// Admin //////////////////////
    @GetMapping("/admin")
    public String AdminPage(Model model){
        List<Utilisateur> users = utilisateurService.afficherUtilisateurs();
        model.addAttribute("utilisateurs", users);
        return "admin/index";
    }

    @GetMapping("/admin/ajoutUtilisateur")
    public String addUserPage(Model model){
        model.addAttribute("utilisateur", new Utilisateur());
        return "admin/ajout";
    }

    @PostMapping("/admin/ajoutUtilisateur")
    public String AddUser(@ModelAttribute("utilisateur") Utilisateur utilisateur){
        utilisateurService.ajouterUtilisateur(utilisateur);
        return "redirect:/utilisateurs/admin";
    }
    @PostMapping("/admin/supprimerUtilisateur/{id}")
    public  String deleteUser(@PathVariable int id, RedirectAttributes redirectAttributes){
        try {
            utilisateurService.supprimerUtilisateur(id);
            redirectAttributes.addFlashAttribute("delete",true);

        }catch (Exception e){
            redirectAttributes.addFlashAttribute("delete", false);
        }
        return "redirect:/utilisateurs/admin";

    }
}
