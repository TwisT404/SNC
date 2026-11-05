package snc.app.Exception;

import org.springframework.http.HttpStatus;

public class RessourceIntrouvableException extends ErreurMetierException {
    public RessourceIntrouvableException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }

    public RessourceIntrouvableException(String message, Throwable cause) {
        super(message, HttpStatus.NOT_FOUND, cause);
    }
}
