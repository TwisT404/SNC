package snc.app.Security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Set;

@ControllerAdvice
public class DroitsAffichageControllerAdvice {
    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final String ROLE_RESPONSABLE = "ROLE_RESPONSABLE";
    private static final String ROLE_CHARGE = "ROLE_CHARGE";

    @ModelAttribute
    public void ajouterDroitsAffichage(Model model, Authentication authentication) {
        Set<String> roles = authentication == null
                ? Set.of()
                : authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(java.util.stream.Collectors.toSet());

        boolean administrateur = roles.contains(ROLE_ADMIN);
        boolean peutGererReferentiels = administrateur || roles.contains(ROLE_RESPONSABLE);
        boolean peutGererIncidents = administrateur
                || roles.contains(ROLE_RESPONSABLE)
                || roles.contains(ROLE_CHARGE);

        model.addAttribute("peutAdministrer", administrateur);
        model.addAttribute("peutConsulterReferentiels",
                peutGererReferentiels || roles.contains(ROLE_CHARGE));
        model.addAttribute("peutGererReferentiels", peutGererReferentiels);
        model.addAttribute("peutVoirJournalActions", administrateur);
        model.addAttribute("peutDeclarerOuAffecter",
                administrateur || roles.contains(ROLE_RESPONSABLE));
        model.addAttribute("peutResoudreIncident", peutGererIncidents);
        model.addAttribute("peutModifierIncident", peutGererIncidents);
        model.addAttribute("peutEvaluer",
                administrateur || roles.contains(ROLE_RESPONSABLE));
    }
}
