package snc.app.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import snc.app.Model.Station;

public interface StationRepository extends JpaRepository<Station, Integer> {
}
