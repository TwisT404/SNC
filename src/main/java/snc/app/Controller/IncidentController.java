package snc.app.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import snc.app.Exception.RessourceIntrouvableException;
import snc.app.Model.Equipement;
import snc.app.Model.Evaluation;
import snc.app.Model.Incident;
import snc.app.Model.Prestataire;
import snc.app.Services.*;

import java.time.Instant;
import java.util.List;

@Controller
@RequestMapping("/incidents")
public class IncidentController {

    private final IncidentService incidentService;
    private final EquipementService  equipementService;
    private final PrestataireService prestataireService;
    private final EvaluationService evaluationService;
    private final HistoriqueService historiqueService;

    public IncidentController(IncidentService incidentService, EquipementService equipementService, PrestataireService prestataireService, EvaluationService evaluationService, HistoriqueService historiqueService) {
        this.incidentService = incidentService;
        this.equipementService = equipementService;
        this.prestataireService = prestataireService;
        this.evaluationService = evaluationService;
        this.historiqueService = historiqueService;
    }

    @GetMapping("/dashboard")
    public String dashboard(@RequestParam(required = false) String color, Model model,
                            Authentication authentication) {
        List<Incident> incidentsNonResolus = incidentService.afficherIncidentsNonResolus(color);
        List<Prestataire> prestataires = prestataireService.afficherPrestataires();
        model.addAttribute("incidents_non_resolus", incidentsNonResolus);
        model.addAttribute("prestataires", prestataires);
        model.addAttribute("maintenant", Instant.now());
        model.addAttribute("evaluation", new Evaluation());
        model.addAttribute("peutDeclarerOuAffecter", possedeUnDesProfils(authentication,
                "ROLE_ADMIN", "ROLE_RESPONSABLE"));
        return "dashboard";
    }

    private boolean possedeUnDesProfils(Authentication authentication, String... profils) {
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> List.of(profils).contains(authority.getAuthority()));
    }

    @PostMapping("/dashboard/{id}")
    public String ajoutePrestataire(@PathVariable int id, @RequestParam("idPrestataire") int idPrestataire, RedirectAttributes redirectAttributes)
    {
        try {
            Incident incident = incidentService.afficherIncident(id);
            if(incident == null){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "L'incident " + id + " n'existe pas");
            }
            List<Prestataire> prestataires = prestataireService.afficherPrestataires();
            if (idPrestataire < 0 || idPrestataire >= prestataires.size()) {
                throw new RessourceIntrouvableException("Le prestataire sélectionné n'existe pas.");
            }

            incidentService.modifierIncident(incident,"Affecté", prestataires.get(idPrestataire));
            redirectAttributes.addFlashAttribute("add_presta_incident", true);
            historiqueService.ajouterHistorique("Ajout Prestataire", "Incident");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("add_presta_incident", false);
            throw new RuntimeException(e);
        }

        return  "redirect:/incidents/dashboard";
    }

    @GetMapping("/declarer")
    public String declarer(Model model){
        try {
            List<Equipement> equipements = equipementService.afficherEquipements();
            List<Prestataire> prestataires = prestataireService.afficherPrestataires();
            model.addAttribute("incident", new Incident());
            model.addAttribute("equipements", equipements);
            model.addAttribute("prestataires", prestataires);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "incidents/declarer";
    }

    @PostMapping("/declarer/envoie")
    public String declarerEnvoie(@ModelAttribute("incident") Incident incident, RedirectAttributes redirectAttributes){
        incident.setStatut("Non Résolu");
        incident.setDateOuverture(Instant.now());
        try {
            incidentService.ajouterIncident(incident);
            redirectAttributes.addFlashAttribute("ajout_incident",true);
            historiqueService.ajouterHistorique("Ajout Incident", "Incident");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("ajout_incident", false);
            throw new RuntimeException(e);
        }
        return  "redirect:/incidents/dashboard";
    }

    @PostMapping("dashboard/{id}/resolution")
    public String resolution(@PathVariable int id, RedirectAttributes redirectAttributes){
        incidentService.mettreStatutResolu(id);
        redirectAttributes.addFlashAttribute("resolution",true);
        return "redirect:/incidents/" + id + "/details";
    }

    @GetMapping("/historique")
    public String historique(Model model){
        List<Incident> incidentsResolus = incidentService.afficherIncidentsResolus();
        model.addAttribute("incidentsResolus", incidentsResolus);
        return "incidents/historique";
    }

    @GetMapping("/{id}/details")
    public String details(@PathVariable int id, Model model, Authentication authentication){
        Incident incident = incidentService.afficherIncident(id);
        Evaluation eval =  evaluationService.afficherEvaluation(id);
        model.addAttribute("incident", incident);
        model.addAttribute("evaluation", eval);
        model.addAttribute("nouvelleEvaluation", new Evaluation());
        model.addAttribute("peutEvaluer", authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN")
                        || authority.getAuthority().equals("ROLE_RESPONSABLE")));
        return "incidents/detail";
    }

    @PostMapping("/{id}/evaluation")
    public String evaluerIncident(@PathVariable int id, @ModelAttribute Evaluation evaluation,
                                  RedirectAttributes redirectAttributes) {
        try {
            evaluationService.ajouterEvaluation(id, evaluation);
            redirectAttributes.addFlashAttribute("evaluationAjoutee", true);
        } catch (IllegalArgumentException | IllegalStateException exception) {
            redirectAttributes.addFlashAttribute("erreurEvaluation", exception.getMessage());
        }
        return "redirect:/incidents/" + id + "/details";
    }

    @PostMapping("/{id}/modifier")
    public String modifierIncident(@PathVariable int id,@RequestParam String description){
        try {
            incidentService.modifierDescriptionIncident(id, description);
            historiqueService.ajouterHistorique("Modifier Incident", "Incident");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "redirect:/incidents/"+id+"/details";

    }

}
