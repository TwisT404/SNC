package snc.app.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import snc.app.Model.Incident;

public interface IncidentRepository extends JpaRepository<Incident, Integer> {
}
