package snc.app.Exception;

import org.springframework.http.HttpStatus;

public class RessourceIntrouvableException extends ErreurMetierException {
    public RessourceIntrouvableException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
