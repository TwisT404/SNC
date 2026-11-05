package snc.app.Exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GestionnaireExceptions {
    private static final Logger LOGGER = LoggerFactory.getLogger(GestionnaireExceptions.class);
    private static final String MESSAGE_DONNEES_INVALIDES = "Les données envoyées sont invalides.";
    private static final String MESSAGE_ERREUR_INATTENDUE =
            "Une erreur inattendue s'est produite. Veuillez réessayer.";

    @ExceptionHandler(ErreurMetierException.class)
    public Object gererErreurMetier(ErreurMetierException exception, HttpServletRequest requete) {
        return creerReponse(exception.getStatutHttp(), exception.getMessage(), requete);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public Object gererConflitBaseDeDonnees(
            DataIntegrityViolationException exception,
            HttpServletRequest requete
    ) {
        String message = "L'opération est impossible car ces données existent déjà ou sont utilisées.";
        LOGGER.warn("Conflit de données pendant {} {}", requete.getMethod(), requete.getRequestURI(), exception);
        return creerReponse(HttpStatus.CONFLICT, message, requete);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public Object gererRouteIntrouvable(HttpServletRequest requete) {
        return creerReponse(HttpStatus.NOT_FOUND, "La page demandée n'existe pas.", requete);
    }

    @ExceptionHandler({
            ErrorResponseException.class,
            HttpMediaTypeException.class,
            HttpRequestMethodNotSupportedException.class
    })
    public Object gererErreurHttp(Exception exception, HttpServletRequest requete) {
        if (!(exception instanceof ErrorResponse erreurHttp)) {
            return gererErreurInattendue(exception, requete);
        }

        String message = erreurHttp.getBody().getDetail();
        if (message == null || message.isBlank()) {
            message = "La requête n'a pas pu être traitée.";
        }
        return creerReponse(erreurHttp.getStatusCode(), message, requete);
    }

    @ExceptionHandler({
            MissingServletRequestParameterException.class,
            MethodArgumentTypeMismatchException.class,
            HttpMessageNotReadableException.class,
            BindException.class
    })
    public Object gererDonneesDeRequeteInvalides(HttpServletRequest requete) {
        return creerReponse(HttpStatus.BAD_REQUEST, MESSAGE_DONNEES_INVALIDES, requete);
    }

    @ExceptionHandler(Exception.class)
    public Object gererErreurInattendue(Exception exception, HttpServletRequest requete) {
        LOGGER.error("Erreur inattendue pour {} {}", requete.getMethod(), requete.getRequestURI(), exception);
        return creerReponse(HttpStatus.INTERNAL_SERVER_ERROR, MESSAGE_ERREUR_INATTENDUE, requete);
    }

    private Object creerReponse(HttpStatusCode statut, String message, HttpServletRequest requete) {
        if (requete.getRequestURI().startsWith("/api/")) {
            return ResponseEntity.status(statut)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ReponseErreur(message));
        }

        boolean introuvable = statut.value() == HttpStatus.NOT_FOUND.value();
        ModelAndView modeleEtVue = new ModelAndView(introuvable ? "error/404" : "error/erreur");
        modeleEtVue.setStatus(statut);
        modeleEtVue.addObject("message", message);
        return modeleEtVue;
    }

    public record ReponseErreur(String message) {
    }
}
