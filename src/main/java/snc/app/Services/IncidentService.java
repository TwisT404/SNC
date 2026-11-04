package snc.app.Services;


import org.springframework.stereotype.Service;
import snc.app.Model.Incident;
import snc.app.Model.Prestataire;
import snc.app.Repository.IncidentRepository;

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

    public IncidentService(IncidentRepository incidentRepository) {
        this.incidentRepository = incidentRepository;
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

    public void mettreStatutResolu(int id) {
        Incident incident = incidentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("L'incident demandé n'existe pas."));
        if (incident.getIdPrestataire() == null) {
            throw new IllegalStateException("Un prestataire doit être affecté avant la résolution.");
        }
        if ("Résolu".equals(incident.getStatut())) {
            throw new IllegalStateException("Cet incident est déjà résolu.");
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
