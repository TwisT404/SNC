package snc.app.Services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import snc.app.Exception.ConflitDonneesException;
import snc.app.Exception.DonneeInvalideException;
import snc.app.Exception.RessourceIntrouvableException;
import snc.app.Model.Utilisateur;
import snc.app.Repository.UtilisateurRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UtilisateurService {
    //attributes
    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    //Constructor
    public UtilisateurService(
            UtilisateurRepository utilisateurRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<Utilisateur> verifierIdentifiants(String identifiant, String motDePasse) {
        Utilisateur utilisateur = utilisateurRepository.findByIdentifiantConnexion(identifiant);
        if (utilisateur == null || !passwordEncoder.matches(motDePasse, utilisateur.getMotDePasse())) {
            return Optional.empty();
        }
        return Optional.of(utilisateur);
    }

    public Boolean IsUserValid(String username, String password) {
        return verifierIdentifiants(username, password).isPresent();
    }

    public void ajouterUtilisateur(Utilisateur utilisateur){
        validerUtilisateur(utilisateur, true);
        if (utilisateurRepository.findByIdentifiantConnexion(utilisateur.getIdentifiantConnexion()) != null) {
            throw new ConflitDonneesException("Cet identifiant de connexion est déjà utilisé.");
        }
        utilisateur.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));
        utilisateurRepository.save(utilisateur);
    }

    public List<Utilisateur> afficherUtilisateurs() {
        return utilisateurRepository.findAll();
    }

    public void supprimerUtilisateur(int id) {
        afficherUtilisateur(id);
        utilisateurRepository.deleteById(id);
    }

    public Utilisateur afficherUtilisateur(int id){
        return utilisateurRepository.findById(id)
                .orElseThrow(() -> new RessourceIntrouvableException("L'utilisateur demandé n'existe pas."));
    }

    public void modifierUtilisateur(Utilisateur userCurrentData) {
        validerUtilisateur(userCurrentData, false);
        Utilisateur utilisateurExistant = utilisateurRepository
                .findByIdentifiantConnexion(userCurrentData.getIdentifiantConnexion());
        if (utilisateurExistant != null && !utilisateurExistant.getId().equals(userCurrentData.getId())) {
            throw new ConflitDonneesException("Cet identifiant de connexion est déjà utilisé.");
        }
        utilisateurRepository.save(userCurrentData);
    }

    private void validerUtilisateur(Utilisateur utilisateur, boolean motDePasseObligatoire) {
        if (utilisateur == null
                || utilisateur.getIdentifiantConnexion() == null
                || utilisateur.getIdentifiantConnexion().isBlank()
                || utilisateur.getPrenom() == null
                || utilisateur.getPrenom().isBlank()
                || utilisateur.getProfil() == null
                || !List.of("admin", "responsable", "charge").contains(utilisateur.getProfil())
                || (motDePasseObligatoire
                    && (utilisateur.getMotDePasse() == null || utilisateur.getMotDePasse().isBlank()))) {
            throw new DonneeInvalideException("Les informations de l'utilisateur sont incomplètes ou invalides.");
        }
    }
}
