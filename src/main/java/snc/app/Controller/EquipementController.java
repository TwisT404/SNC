package snc.app.Controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import snc.app.Exception.ErreurMetierException;
import snc.app.Model.Equipement;
import snc.app.Model.Incident;
import snc.app.Model.Station;
import snc.app.Services.EquipementService;
import snc.app.Services.IncidentService;
import snc.app.Services.StationService;

import java.util.List;

@Controller
@RequestMapping("/equipements")
public class EquipementController {

    private final EquipementService equipementService;
    private final StationService stationService;
    private  final IncidentService incidentService;

    public EquipementController(EquipementService equipementService, StationService stationService,
                                IncidentService incidentService) {
        this.equipementService = equipementService;
        this.stationService = stationService;
        this.incidentService = incidentService;
    }

    @GetMapping("")
    public String index(Model model, Authentication authentication) {

        List<Station> stations = stationService.afficherStations();
        List<Equipement> equipement = equipementService.afficherEquipements();
        model.addAttribute("stations", stations);
        model.addAttribute("equipements", equipement);
        model.addAttribute("peutAjouterEquipement", authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN")
                        || authority.getAuthority().equals("ROLE_RESPONSABLE")));
        return "equipements/index";
    }

    @GetMapping("/ajouter")
    public String formulaireAjout(Model model) {
        model.addAttribute("equipement", new Equipement());
        model.addAttribute("stations", stationService.afficherStations());
        return "equipements/ajouter";
    }

    @PostMapping("/ajouter")
    public String ajouter(@RequestParam(required = false) String referenceUnique,
                          @RequestParam(required = false) String libelle,
                          @RequestParam(required = false) String type,
                          @RequestParam(required = false) String etatFonctionnement,
                          @RequestParam(required = false) Integer stationId,
                          Model model, RedirectAttributes redirectAttributes) {
        try {
            equipementService.ajouterEquipement(
                    referenceUnique, libelle, type, etatFonctionnement, stationId);
            redirectAttributes.addFlashAttribute("ajoutEquipement", true);
            return "redirect:/equipements";
        } catch (ErreurMetierException erreur) {
            Equipement equipement = new Equipement();
            equipement.setReferenceUnique(referenceUnique);
            equipement.setLibelle(libelle);
            equipement.setType(type);
            equipement.setEtatFonctionnement(etatFonctionnement);
            model.addAttribute("equipement", equipement);
            model.addAttribute("stationId", stationId);
            model.addAttribute("stations", stationService.afficherStations());
            model.addAttribute("erreurAjoutEquipement", erreur.getMessage());
            return "equipements/ajouter";
        }
    }

    @GetMapping("/details/{id}")
    public String GetDetails(@PathVariable int id, Model model){

        Equipement equipement = equipementService.afficherEquipement(id);
        List<Incident> incidents = incidentService.afficherIncidentsProvenantDeEquipement(equipement.getId());

        model.addAttribute("incidents", incidents);
        model.addAttribute("equipement", equipement);

        return "equipements/details";
    }
}
