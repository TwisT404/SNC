package snc.app.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import snc.app.Model.Evaluation;

public interface EvaluationRepository extends JpaRepository<Evaluation, Integer> {
}
