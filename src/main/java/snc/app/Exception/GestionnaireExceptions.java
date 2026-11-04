package snc.app.Exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GestionnaireExceptions {
    private static final Logger LOGGER = LoggerFactory.getLogger(GestionnaireExceptions.class);
    private static final String MESSAGE_DONNEES_INVALIDES = "Les données envoyées sont invalides.";
    private static final String MESSAGE_ERREUR_INATTENDUE =
            "Une erreur inattendue s'est produite. Veuillez réessayer.";

    @ExceptionHandler(ErreurMetierException.class)
    public ResponseEntity<?> gererErreurMetier(ErreurMetierException exception, HttpServletRequest requete) {
        return creerReponse(exception.getStatutHttp(), exception.getMessage(), requete);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> gererConflitBaseDeDonnees(HttpServletRequest requete) {
        String message = "L'opération est impossible car ces données existent déjà ou sont utilisées.";
        return creerReponse(HttpStatus.CONFLICT, message, requete);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<?> gererRouteIntrouvable(HttpServletRequest requete) {
        return creerReponse(HttpStatus.NOT_FOUND, "La page demandée n'existe pas.", requete);
    }

    @ExceptionHandler({
            MissingServletRequestParameterException.class,
            MethodArgumentTypeMismatchException.class,
            HttpMessageNotReadableException.class,
            BindException.class
    })
    public ResponseEntity<?> gererDonneesDeRequeteInvalides(HttpServletRequest requete) {
        return creerReponse(HttpStatus.BAD_REQUEST, MESSAGE_DONNEES_INVALIDES, requete);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> gererErreurInattendue(Exception exception, HttpServletRequest requete) {
        LOGGER.error("Erreur inattendue pour {} {}", requete.getMethod(), requete.getRequestURI(), exception);
        return creerReponse(HttpStatus.INTERNAL_SERVER_ERROR, MESSAGE_ERREUR_INATTENDUE, requete);
    }

    private ResponseEntity<?> creerReponse(HttpStatus statut, String message, HttpServletRequest requete) {
        if (requete.getRequestURI().startsWith("/api/")) {
            return ResponseEntity.status(statut)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ReponseErreur(message));
        }
        return ResponseEntity.status(statut)
                .contentType(MediaType.TEXT_PLAIN)
                .body(message);
    }

    public record ReponseErreur(String message) {
    }
}
