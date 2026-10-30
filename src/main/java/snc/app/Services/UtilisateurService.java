package snc.app.Services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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
        utilisateur.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));
        utilisateurRepository.save(utilisateur);
    }

    public List<Utilisateur> afficherUtilisateurs() {
        return utilisateurRepository.findAll();
    }

    public void supprimerUtilisateur(int id) {
        utilisateurRepository.deleteById(id);
    }

    public Utilisateur afficherUtilisateur(int id){
        return utilisateurRepository.findById(id).isPresent() ? utilisateurRepository.findById(id).get() : null;
    }

    public void modifierUtilisateur(Utilisateur userCurrentData) {
        utilisateurRepository.save(userCurrentData);
    }
}
