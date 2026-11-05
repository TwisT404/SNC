package snc.app.Exception;

import org.springframework.http.HttpStatus;

public class ConflitDonneesException extends ErreurMetierException {
    public ConflitDonneesException(String message) {
        super(message, HttpStatus.CONFLICT);
    }

    public ConflitDonneesException(String message, Throwable cause) {
        super(message, HttpStatus.CONFLICT, cause);
    }
}
