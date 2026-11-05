package snc.app.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import snc.app.Model.Evaluation;
import snc.app.Model.Historique;

import java.util.List;

public interface HistoriqueRepository extends JpaRepository<Historique, Integer> {
}
