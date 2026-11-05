package snc.app.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import snc.app.Model.Historique;
import snc.app.Services.HistoriqueService;

import java.util.List;

@Controller
@RequestMapping("/historique")
public class HistoriqueController {

    private final HistoriqueService historiqueService;

    public HistoriqueController(HistoriqueService historiqueService) {
        this.historiqueService = historiqueService;
    }

    @GetMapping("")
    public String historique(Model model) {
        List<Historique> historiques = historiqueService.afficherHistoriques();
        model.addAttribute("historiques", historiques);
        return "historique";
    }
}
