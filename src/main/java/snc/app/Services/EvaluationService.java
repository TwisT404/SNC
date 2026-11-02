package snc.app.Services;

import org.springframework.stereotype.Service;
import snc.app.Model.Evaluation;
import snc.app.Repository.EvaluationRepository;

@Service
public class EvaluationService {

    private final EvaluationRepository evaluationRepository;

    public EvaluationService(EvaluationRepository evaluationRepository) {
        this.evaluationRepository = evaluationRepository;
    }

    public void ajouterEvaluation(Evaluation eval) {
        evaluationRepository.save(eval);
    }
}
