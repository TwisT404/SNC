package snc.app.Services;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import snc.app.Model.Utilisateur;
import snc.app.Repository.UtilisateurRepository;

import java.util.List;

@Service
public class UtilisateurService {
    //attributes
    private final UtilisateurRepository utilisateurRepository;

    //Constructor
    public UtilisateurService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    public Boolean IsUserValid(String username, String password){
        Utilisateur utilisateur = utilisateurRepository.findByIdentifiantConnexion(username);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (utilisateur == null) {
            return false;
        }
        return encoder.matches(password, utilisateur.getMotDePasse());
    }

    public void ajouterUtilisateur(Utilisateur utilisateur){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String mdp = encoder.encode(utilisateur.getMotDePasse());
        utilisateur.setMotDePasse(mdp);
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
