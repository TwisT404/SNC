package snc.app.Services;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import snc.app.Model.Evaluation;
import snc.app.Model.Incident;
import snc.app.Model.Prestataire;
import snc.app.Repository.EvaluationRepository;
import snc.app.Repository.IncidentRepository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    public List<Incident> afficherIncidentsNonResolus(){
        List<String> status = new ArrayList<String>();
        status.add("Non résolu");
        status.add("Affecté");
        return incidentRepository.findByStatutInOrderByDateOuvertureDesc(status);
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
