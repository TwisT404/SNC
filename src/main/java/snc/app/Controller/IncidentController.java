package snc.app.Controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import snc.app.Exception.ErreurMetierException;
import snc.app.Exception.RessourceIntrouvableException;
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
            List<Prestataire> prestataires = prestataireService.afficherPrestataires();
            if (idPrestataire < 0 || idPrestataire >= prestataires.size()) {
                throw new RessourceIntrouvableException("Le prestataire sélectionné n'existe pas.");
            }
            incidentService.modifierIncident(incident, "Affecté", prestataires.get(idPrestataire));
            redirectAttributes.addFlashAttribute("add_presta_incident", true);
            historiqueService.ajouterHistorique("Ajout Prestataire", "Incident");
        } catch (ErreurMetierException exception) {
            redirectAttributes.addFlashAttribute("erreur_affectation", exception.getMessage());
        }

        return "redirect:/incidents/dashboard";
    }

    @GetMapping("/declarer")
    public String declarer(Model model){
        if (!model.containsAttribute("incident")) {
            model.addAttribute("incident", new Incident());
        }
        return afficherFormulaireDeclaration(model);
    }

    @PostMapping("/declarer/envoie")
    public String declarerEnvoie(@ModelAttribute("incident") Incident incident, Model model,
                                 RedirectAttributes redirectAttributes) {
        incident.setStatut("Non Résolu");
        incident.setDateOuverture(Instant.now());
        try {
            incidentService.ajouterIncident(incident);
            redirectAttributes.addFlashAttribute("ajout_incident", true);
            return "redirect:/incidents/dashboard";
        } catch (ErreurMetierException erreur) {
            model.addAttribute("incident", incident);
            model.addAttribute("erreur_declaration", erreur.getMessage());
            return afficherFormulaireDeclaration(model);
        }
    }

    private String afficherFormulaireDeclaration(Model model) {
        model.addAttribute("equipements", equipementService.afficherEquipements());
        return "incidents/declarer";
    }

    @PostMapping("dashboard/{id}/resolution")
    public String resolution(@PathVariable int id, RedirectAttributes redirectAttributes){
        try {
            incidentService.mettreStatutResolu(id);
            redirectAttributes.addFlashAttribute("resolution", true);
            return "redirect:/incidents/" + id + "/details";
        } catch (ErreurMetierException exception) {
            redirectAttributes.addFlashAttribute("erreur_resolution", exception.getMessage());
            return "redirect:/incidents/dashboard";
        }
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
        if (!model.containsAttribute("nouvelleEvaluation")) {
            model.addAttribute("nouvelleEvaluation", new Evaluation());
        }
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
        } catch (ErreurMetierException exception) {
            redirectAttributes.addFlashAttribute("erreurEvaluation", exception.getMessage());
            redirectAttributes.addFlashAttribute("nouvelleEvaluation", evaluation);
        }
        return "redirect:/incidents/" + id + "/details";
    }

    @PostMapping("/{id}/modifier")
    public String modifierIncident(@PathVariable int id, @RequestParam String description,
                                   RedirectAttributes redirectAttributes) {
        try {
            incidentService.modifierDescriptionIncident(id, description);
            historiqueService.ajouterHistorique("Modifier Incident", "Incident");
        } catch (ErreurMetierException exception) {
            redirectAttributes.addFlashAttribute("erreurModification", exception.getMessage());
            redirectAttributes.addFlashAttribute("descriptionSaisie", description);
        }
        return "redirect:/incidents/" + id + "/details";
    }

}
