package snc.app.Services;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;
import snc.app.Model.Historique;
import snc.app.Model.Utilisateur;
import snc.app.Repository.HistoriqueRepository;

import java.util.List;

@Service
public class HistoriqueService {

    private final HistoriqueRepository historiqueRepository;
    private final UtilisateurService utilisateurService;

    public HistoriqueService(HistoriqueRepository historiqueRepository, UtilisateurService utilisateurService) {
        this.historiqueRepository = historiqueRepository;
        this.utilisateurService = utilisateurService;
    }

    public List<Historique> findAllByUtilisateurId(int idUtilisateur) {
        return historiqueRepository.findAllByUtilisateurId(idUtilisateur);
    }

    public void ajouterHistorique(int utilisateurId, String action, String entite_concerne) {
        Historique historique = new Historique();
        Utilisateur user = utilisateurService.afficherUtilisateur(utilisateurId);
        if (user == null) {
            throw new IllegalStateException("L'utilisateur n'existe pas");
        }
        historique.setUtilisateurId(utilisateurId);
        historique.setAction(action);
    }
}
