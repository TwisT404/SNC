package snc.app.Services;

import org.springframework.stereotype.Service;
import snc.app.Model.Evaluation;
import snc.app.Model.Incident;
import snc.app.Repository.EvaluationRepository;
import snc.app.Repository.IncidentRepository;

@Service
public class EvaluationService {

    private final EvaluationRepository evaluationRepository;
    private final IncidentRepository incidentRepository;

    public EvaluationService(EvaluationRepository evaluationRepository, IncidentRepository incidentRepository) {
        this.evaluationRepository = evaluationRepository;
        this.incidentRepository = incidentRepository;
    }

    public void ajouterEvaluation(int idIncident, Evaluation evaluation) {
        Incident incident = incidentRepository.findById(idIncident)
                .orElseThrow(() -> new IllegalArgumentException("L'incident demandé n'existe pas."));
        if (!"Résolu".equals(incident.getStatut())) {
            throw new IllegalStateException("L'incident doit être résolu avant son évaluation.");
        }
        if (evaluationRepository.findByIdIncident_Id(idIncident).isPresent()) {
            throw new IllegalStateException("Cet incident a déjà été évalué.");
        }
        if (evaluation == null || evaluation.getNote() == null
                || evaluation.getNote() < 1 || evaluation.getNote() > 5) {
            throw new IllegalArgumentException("La note doit être comprise entre 1 et 5.");
        }

        evaluation.setIdIncident(incident);
        evaluationRepository.save(evaluation);
    }

    public Evaluation afficherEvaluation(int idIncident) {
        return evaluationRepository.findByIdIncident_Id(idIncident).orElse(null);
    }
}
