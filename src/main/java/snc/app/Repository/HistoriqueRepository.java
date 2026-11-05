package snc.app.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import snc.app.Model.Historique;

public interface HistoriqueRepository extends JpaRepository<Historique, Integer> {
}
