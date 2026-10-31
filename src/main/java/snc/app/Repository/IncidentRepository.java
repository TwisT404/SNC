package snc.app.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import snc.app.Model.Equipement;
import snc.app.Model.Incident;
import snc.app.Model.Utilisateur;

import java.util.List;

public interface IncidentRepository extends JpaRepository<Incident, Integer> {
    List<Incident> findIncidentByIdEquipement_Id(Integer idEquipementId);
}
