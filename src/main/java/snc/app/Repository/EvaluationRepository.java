package snc.app.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import snc.app.Model.Evaluation;

import java.util.Optional;

public interface EvaluationRepository extends JpaRepository<Evaluation, Integer> {
    Optional<Evaluation> findByIdIncident_Id(Integer idIncident);
}
