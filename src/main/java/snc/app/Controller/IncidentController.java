package snc.app.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import snc.app.Model.Equipement;
import snc.app.Model.Evaluation;
import snc.app.Model.Incident;
import snc.app.Model.Prestataire;
import snc.app.Services.EquipementService;
import snc.app.Services.EvaluationService;
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
    private final EvaluationService evaluationService;

    public IncidentController(IncidentService incidentService, EquipementService equipementService, PrestataireService prestataireService, EvaluationService evaluationService) {
        this.incidentService = incidentService;
        this.equipementService = equipementService;
        this.prestataireService = prestataireService;
        this.evaluationService = evaluationService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model){
        List<Incident> incidentsNonResolus = incidentService.afficherIncidentsNonResolus();
        List<Prestataire> prestataires = prestataireService.afficherPrestataires();
        model.addAttribute("incidents_non_resolus", incidentsNonResolus);
        model.addAttribute("prestataires", prestataires);
        model.addAttribute("evaluation", new Evaluation());
        return "dashboard";
    }

    @PostMapping("/dashboard/{id}")
    public String ajoutePrestataire(@PathVariable int id, @RequestParam("idPrestataire") int idPrestataire, RedirectAttributes redirectAttributes)
    {
        Incident incident = incidentService.afficherIncident(id);
        if(incident == null){
            return "error/404";
        }
        Prestataire prestataire = prestataireService.afficherPrestataires().get(idPrestataire);

        incidentService.modifierIncident(incident,"Affecté", prestataire);
        redirectAttributes.addFlashAttribute("add_presta_incident", true);
        return  "redirect:/incidents/dashboard";
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

    @PostMapping("dashboard/{id}/resolution")
    public String resolution(@PathVariable int id, @ModelAttribute Evaluation eval, RedirectAttributes redirectAttributes){
        try {
            incidentService.mettreStatutResolu(id, eval);
            redirectAttributes.addFlashAttribute("resolution",true);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return "redirect:/incidents/dashboard";
    }

    @GetMapping("/historique")
    public String historique(Model model){
        List<Incident> incidentsResolus = incidentService.afficherIncidentsResolus();
        model.addAttribute("incidentsResolus", incidentsResolus);
        return "incidents/historique";
    }

    @GetMapping("/{id}/details")
    public String details(@PathVariable int id, Model model){
        Incident incident = incidentService.afficherIncident(id);
        Evaluation eval =  evaluationService.afficherEvaluation(id);
        model.addAttribute("incident", incident);
        model.addAttribute("evaluation", eval);
        return "incidents/detail";
    }

    @PostMapping("/{id}/modifier")
    public String modifierIncident(@PathVariable int id,@RequestParam String description){
        try {
            incidentService.modifierDescriptionIncident(id, description);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "redirect:/incidents/"+id+"/details";

    }

}
