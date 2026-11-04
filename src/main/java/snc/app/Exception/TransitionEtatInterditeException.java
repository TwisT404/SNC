package snc.app.Exception;

import org.springframework.http.HttpStatus;

public class TransitionEtatInterditeException extends ErreurMetierException {
    public TransitionEtatInterditeException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
