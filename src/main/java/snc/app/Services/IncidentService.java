package snc.app.Services;


import org.springframework.stereotype.Service;
import snc.app.Exception.DonneeInvalideException;
import snc.app.Exception.RessourceIntrouvableException;
import snc.app.Exception.TransitionEtatInterditeException;
import snc.app.Model.Incident;
import snc.app.Model.Prestataire;
import snc.app.Repository.EquipementRepository;
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
    private final EquipementRepository equipementRepository;

    public IncidentService(IncidentRepository incidentRepository, EquipementRepository equipementRepository) {
        this.incidentRepository = incidentRepository;
        this.equipementRepository = equipementRepository;
    }


    public Incident afficherIncident(int id){
        return incidentRepository.findById(id)
                .orElseThrow(() -> new RessourceIntrouvableException("L'incident " + id + " n'existe pas."));
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
        if (incident == null
                || incident.getDescription() == null
                || incident.getDescription().isBlank()
                || incident.getIdEquipement() == null
                || incident.getIdEquipement().getId() == null) {
            throw new DonneeInvalideException(
                    "Une description et un équipement sont obligatoires pour déclarer un incident.");
        }
        if (!equipementRepository.existsById(incident.getIdEquipement().getId())) {
            throw new RessourceIntrouvableException("L'équipement sélectionné n'existe pas.");
        }
        incidentRepository.save(incident);
    }

    public void modifierIncident(Incident incident, String statut, Prestataire prestataire) {
        if (incident == null || prestataire == null) {
            throw new DonneeInvalideException("Un incident et un prestataire doivent être sélectionnés.");
        }
        if ("Résolu".equals(incident.getStatut())) {
            throw new TransitionEtatInterditeException("Un incident résolu ne peut plus être réaffecté.");
        }
        incident.setIdPrestataire(prestataire);
        incident.setStatut(statut);
        incidentRepository.save(incident);
    }

    public void mettreStatutResolu(int id) {
        Incident incident = incidentRepository.findById(id)
                .orElseThrow(() -> new RessourceIntrouvableException("L'incident demandé n'existe pas."));
        if (incident.getIdPrestataire() == null) {
            throw new TransitionEtatInterditeException("Un prestataire doit être affecté avant la résolution.");
        }
        if ("Résolu".equals(incident.getStatut())) {
            throw new TransitionEtatInterditeException("Cet incident est déjà résolu.");
        }
        incident.setStatut("Résolu");
        incident.setDateResolution(Instant.now());
        incidentRepository.save(incident);
    }

    public List<Incident> afficherIncidentsResolus() {
        return incidentRepository.findIncidentByStatutOrderByDateOuvertureDesc("Résolu");
    }

    public void modifierDescriptionIncident(int id, String description) {
        if (description == null || description.isBlank()) {
            throw new DonneeInvalideException("La description de l'incident ne peut pas être vide.");
        }
        Incident incident = afficherIncident(id);
        incident.setDescription(description);
        incidentRepository.save(incident);
    }
}
