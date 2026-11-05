package snc.app.Controller;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import snc.app.Model.Prestataire;
import snc.app.Services.PrestataireService;

import java.util.List;

@Controller
@RequestMapping("/prestataires")
public class PrestataireController {
    private final PrestataireService prestataireService;

    public PrestataireController(PrestataireService prestataireService) {

        this.prestataireService = prestataireService;
    }

    @GetMapping("")
    public String prestataire(Model model) {
        List<Prestataire> prestataires = prestataireService.afficherPrestataires();
        model.addAttribute("prestataires", prestataires);
        return "prestataires/index";
    }

    @GetMapping("/ajout")
    public String ajouterPrestataire(Model model) {
        model.addAttribute("prestataire", new Prestataire());
        return "prestataires/ajout";
    }

    @PostMapping("/ajout/envoie")
    public String ajoutEnvoie(@ModelAttribute("prestataire") Prestataire prestataire){
        prestataireService.ajouterPrestataire(prestataire);
        return "redirect:/prestataires";
    }
}
