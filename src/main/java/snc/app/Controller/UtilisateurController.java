package snc.app.Controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import snc.app.Model.Utilisateur;
import snc.app.Services.AuthentificationService;
import snc.app.Services.UtilisateurService;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/utilisateurs")
public class UtilisateurController {

    private final UtilisateurService utilisateurService;
    private final AuthentificationService authentificationService;

    public UtilisateurController(
            UtilisateurService utilisateurService,
            AuthentificationService authentificationService
    ) {
        this.utilisateurService = utilisateurService;
        this.authentificationService = authentificationService;
    }

    @GetMapping("/")
    public String redirigerVersConnexion() {
        return "redirect:/login";
    }

    @PostMapping("/login")
    public String connecter(
            @RequestParam String username,
            @RequestParam String password,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        if (username.isBlank() || password.isBlank()) {
            return "redirect:/login?error=empty";
        }

        return authentificationService.connecter(username, password)
                .map(jeton -> {
                    ResponseCookie cookie = ResponseCookie.from(
                                    AuthentificationService.NOM_COOKIE_JWT, jeton.jeton())
                            .httpOnly(true)
                            .secure(request.isSecure())
                            .sameSite("Lax")
                            .path("/")
                            .maxAge(Duration.between(Instant.now(), jeton.expiration()))
                            .build();
                    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
                    return "redirect:/incidents/dashboard";
                })
                .orElse("redirect:/login?error=authentification");
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
    public String AddUser(@ModelAttribute("utilisateur") Utilisateur utilisateur, RedirectAttributes redirectAttributes){
        try {
            utilisateurService.ajouterUtilisateur(utilisateur);
            redirectAttributes.addFlashAttribute("create", "success");
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("create", "error");
        }
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

    @GetMapping("/admin/modifierUtilisateur")
    public String updateUserPage(@RequestParam("id") int id, Model model, RedirectAttributes redirectAttributes){
        Utilisateur user = utilisateurService.afficherUtilisateur(id);
        if(user == null){
            return "error/404";
        }
        model.addAttribute("utilisateur", user);
        return "admin/modifier";
    }

    @PostMapping("/admin/modifierUtilisateur")
    public String updateUser(@RequestParam("id") int id, @ModelAttribute("utilisateur") Utilisateur utilisateur, RedirectAttributes redirectAttributes){
        try {
            Utilisateur userCurrentData = utilisateurService.afficherUtilisateur(id);
            if (userCurrentData == null) {
                return "error/404";
            }

            if (!Objects.equals(userCurrentData.getIdentifiantConnexion(), utilisateur.getIdentifiantConnexion())  && utilisateur.getIdentifiantConnexion() != null) {
                userCurrentData.setIdentifiantConnexion(utilisateur.getIdentifiantConnexion());
            }
            if (!Objects.equals(userCurrentData.getPrenom(), utilisateur.getPrenom()) && utilisateur.getPrenom() != null) {
                userCurrentData.setPrenom(utilisateur.getPrenom());
            }
            if (!Objects.equals(userCurrentData.getProfil(), utilisateur.getProfil())  && utilisateur.getProfil() != null) {
                userCurrentData.setProfil(utilisateur.getProfil());
            }
            utilisateurService.modifierUtilisateur(userCurrentData);
            redirectAttributes.addFlashAttribute("modif", "success");
        }
        catch (Exception e){
            redirectAttributes.addFlashAttribute("modif", "error");
        }

        return "redirect:/utilisateurs/admin";
    }
}
