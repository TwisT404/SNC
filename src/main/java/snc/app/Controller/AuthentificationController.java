package snc.app.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import snc.app.Services.AuthentificationService;
import snc.app.Services.AuthentificationService.JetonResponse;

@RestController
@RequestMapping("/api/auth")
public class AuthentificationController {
    private final AuthentificationService authentificationService;

    public AuthentificationController(AuthentificationService authentificationService) {
        this.authentificationService = authentificationService;
    }

    @PostMapping("/connexion")
    public ResponseEntity<?> connecter(@RequestBody DemandeConnexion demande) {
        if (demande == null || demande.identifiant() == null || demande.identifiant().isBlank()
                || demande.motDePasse() == null || demande.motDePasse().isBlank()) {
            return ResponseEntity.badRequest()
                    .body(new MessageErreur("L’identifiant et le mot de passe sont obligatoires."));
        }

        return authentificationService.connecter(demande.identifiant(), demande.motDePasse())
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new MessageErreur("Identifiant ou mot de passe incorrect.")));
    }

    public record DemandeConnexion(String identifiant, String motDePasse) {
    }

    private record MessageErreur(String message) {
    }
}
