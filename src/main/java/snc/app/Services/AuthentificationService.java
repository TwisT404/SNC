package snc.app.Services;

import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import snc.app.Model.Utilisateur;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Service
public class AuthentificationService {
    private static final Duration DUREE_JETON = Duration.ofHours(1);

    private final UtilisateurService utilisateurService;
    private final JwtEncoder jwtEncoder;

    public AuthentificationService(UtilisateurService utilisateurService, JwtEncoder jwtEncoder) {
        this.utilisateurService = utilisateurService;
        this.jwtEncoder = jwtEncoder;
    }

    public Optional<JetonResponse> connecter(String identifiant, String motDePasse) {
        Optional<Utilisateur> utilisateur = utilisateurService.verifierIdentifiants(identifiant, motDePasse);
        return utilisateur.map(this::creerJeton);
    }

    private JetonResponse creerJeton(Utilisateur utilisateur) {
        Instant maintenant = Instant.now();
        Instant expiration = maintenant.plus(DUREE_JETON);
        JwtClaimsSet revendications = JwtClaimsSet.builder()
                .subject(utilisateur.getIdentifiantConnexion())
                .issuedAt(maintenant)
                .expiresAt(expiration)
                .claim("utilisateurId", utilisateur.getId())
                .claim("profil", utilisateur.getProfil())
                .build();

        String jeton = jwtEncoder.encode(JwtEncoderParameters.from(
                JwsHeader.with(MacAlgorithm.HS256).build(),
                revendications
        )).getTokenValue();

        return new JetonResponse(jeton, expiration);
    }

    public record JetonResponse(String jeton, Instant expiration) {
    }
}
