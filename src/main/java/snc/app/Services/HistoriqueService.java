package snc.app.Services;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import snc.app.Model.Historique;
import snc.app.Model.Utilisateur;
import snc.app.Repository.HistoriqueRepository;

import java.time.Instant;
import java.util.List;

@Service
public class HistoriqueService {

    private final HistoriqueRepository historiqueRepository;
    private final UtilisateurService utilisateurService;

    public HistoriqueService(HistoriqueRepository historiqueRepository, UtilisateurService utilisateurService) {
        this.historiqueRepository = historiqueRepository;
        this.utilisateurService = utilisateurService;
    }


    public void ajouterHistorique(String action, String entite_concerne) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!(auth instanceof JwtAuthenticationToken jwtAuth)) {
            throw new IllegalStateException("Aucun utilisateur authentifié.");
        }

        Jwt jwt = jwtAuth.getToken();
        Number claim = jwt.getClaim("utilisateurId");
        if (claim == null) {
            throw new IllegalStateException("Le token ne contient pas de claim 'utilisateurId'.");
        }
        int utilisateurId = claim.intValue();
        Utilisateur user = utilisateurService.afficherUtilisateur(utilisateurId);
        if (user == null) {
            throw  new IllegalArgumentException("L'utilisateur demandé n'existe pas.");
        }
        Historique historique = new Historique();
        historique.setUtilisateurId(utilisateurId);
        historique.setAction(action);
        historique.setEntiteConcerne(entite_concerne);
        historique.setDateAction(Instant.now());
        historiqueRepository.save(historique);
    }

    public List<Historique> afficherHistoriques() {
        return historiqueRepository.findAll();
    }
}
