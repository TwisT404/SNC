package snc.app.Services;

import org.springframework.stereotype.Service;
import snc.app.Model.Incident;
import snc.app.Model.Prestataire;
import snc.app.Repository.IncidentRepository;

import java.time.Instant;
import java.util.List;

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

    public List<Incident> afficherIncidentsNonResolus(){
        return incidentRepository.findIncidentByStatut("Non résolu");
    }

    public void ajouterIncident(Incident incident) {
        incidentRepository.save(incident);
    }

    public void modifierIncident(Incident incident, String statut, Prestataire prestataire) {
        incident.setIdPrestataire(prestataire);
        incident.setStatut(statut);
        incidentRepository.save(incident);
    }

    public void mettreStatutResolu(int id){
        Incident incident = incidentRepository.findById(id).orElse(null);
        if(incident == null){
            return;
        }
        incident.setStatut("Résolu");
        incident.setDateResolution(Instant.now());
        incidentRepository.save(incident);
    }
}
