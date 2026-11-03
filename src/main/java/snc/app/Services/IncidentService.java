package snc.app.Services;


import org.springframework.stereotype.Service;
import snc.app.Globals;
import snc.app.Model.Evaluation;
import snc.app.Model.Incident;
import snc.app.Model.Prestataire;
import snc.app.Repository.EvaluationRepository;
import snc.app.Repository.IncidentRepository;

import javax.print.DocFlavor;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static snc.app.Globals.JAUNE;
import static snc.app.Globals.ROUGE;

@Service
public class IncidentService {

    private final IncidentRepository incidentRepository;
    private final EvaluationRepository evaluationRepository;

    public IncidentService(IncidentRepository incidentRepository, EvaluationRepository evaluationRepository) {
        this.incidentRepository = incidentRepository;
        this.evaluationRepository = evaluationRepository;
    }


    public Incident afficherIncident(int id){
        return incidentRepository.findById(id).orElse(null);
    }

    public List<Incident> afficherIncidentsProvenantDeEquipement(int id) {
        return incidentRepository.findIncidentByIdEquipement_Id(id);
    }

    public List<Incident> afficherIncidentsNonResolus(String color) {
        List<String> status = new ArrayList<String>();
        status.add("Non résolu");
        status.add("Affecté");
        List<Incident> incidents = incidentRepository.findByStatutInOrderByDateOuvertureDesc(status);
        if (color == null) {
            return incidents;
        }
        switch (color) {
            case "rouge":
                return incidents.stream()
                        .filter(incident -> Duration.between(incident.getDateOuverture(), Instant.now()).toHours() >= ROUGE && Objects.equals(incident.getStatut(), "Non Résolu"))
                        .toList();
            case "jaune":
                return incidents.stream()
                        .filter(incident -> (Duration.between(incident.getDateOuverture(), Instant.now()).toHours() < ROUGE)
                                && Duration.between(incident.getDateOuverture(), Instant.now()).toHours() >= JAUNE && Objects.equals(incident.getStatut(), "Non Résolu"))
                        .toList();
            case "vert":
                return incidents.stream()
                        .filter(incident -> Duration.between(incident.getDateOuverture(), Instant.now()).toHours() < JAUNE && Objects.equals(incident.getStatut(), "Non Résolu"))
                        .toList();
            default :
                return incidents;
        }
    }

    public void ajouterIncident(Incident incident) {
        incidentRepository.save(incident);
    }

    public void modifierIncident(Incident incident, String statut, Prestataire prestataire) {
        incident.setIdPrestataire(prestataire);
        incident.setStatut(statut);
        incidentRepository.save(incident);
    }

    public void mettreStatutResolu(int id, Evaluation eval) {
        Incident incident = incidentRepository.findById(id).orElse(null);
        if(incident == null || incident.getIdPrestataire() == null){
            return;
        }
        if (eval != null && eval.getNote() != null) {
            Evaluation nouvelle = new Evaluation();   // id = null → INSERT
            nouvelle.setNote(eval.getNote());
            nouvelle.setCommentaire(eval.getCommentaire());
            nouvelle.setIdIncident(incident);// ou
            evaluationRepository.save(nouvelle);
        }
        incident.setStatut("Résolu");
        incident.setDateResolution(Instant.now());
        incidentRepository.save(incident);
    }

    public List<Incident> afficherIncidentsResolus() {
        return incidentRepository.findIncidentByStatutOrderByDateOuvertureDesc("Résolu");
    }

    public void modifierDescriptionIncident(int id, String description) {
        Incident incident = incidentRepository.findById(id).orElse(null);
        if(incident == null){
            throw new RuntimeException("L'incident n'existe pas");
        }

        incident.setDescription(description);
        incidentRepository.save(incident);
    }
}
