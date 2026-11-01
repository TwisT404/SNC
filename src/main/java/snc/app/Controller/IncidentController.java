package snc.app.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import snc.app.Model.Equipement;
import snc.app.Model.Incident;
import snc.app.Model.Prestataire;
import snc.app.Services.EquipementService;
import snc.app.Services.IncidentService;
import snc.app.Services.PrestataireService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/incidents")
public class IncidentController {

    private final IncidentService incidentService;
    private final EquipementService  equipementService;
    private final PrestataireService prestataireService;

    public IncidentController(IncidentService incidentService, EquipementService equipementService, PrestataireService prestataireService) {
        this.incidentService = incidentService;
        this.equipementService = equipementService;
        this.prestataireService = prestataireService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model){
        List<Incident> incidentsNonResolus = incidentService.afficherIncidentsNonResolus();
        model.addAttribute("incidents_non_resolus", incidentsNonResolus);
        return "dashboard";
    }

    @GetMapping("/declarer")
    public String declarer(Model model){
        List<Equipement> equipements = equipementService.afficherEquipements();
        List<Prestataire> prestataires = prestataireService.afficherPrestataires();
        model.addAttribute("incident", new Incident());
        model.addAttribute("equipements", equipements);
        model.addAttribute("prestataires", prestataires);
        return "incidents/declarer";
    }

    @PostMapping("/declarer/envoie")
    public String declarerEnvoie(@ModelAttribute("incident") Incident incident, RedirectAttributes redirectAttributes){
        incident.setStatut("Non Résolu");
        incident.setDateOuverture(Instant.now());
        try {
            incidentService.ajouterIncident(incident);
            redirectAttributes.addFlashAttribute("ajout_incident",true);

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("ajout_incident", false);
            throw new RuntimeException(e);
        }
        return  "redirect:/incidents/dashboard";
    }


}
