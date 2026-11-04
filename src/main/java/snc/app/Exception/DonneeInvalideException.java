package snc.app.Exception;

import org.springframework.http.HttpStatus;

public class DonneeInvalideException extends ErreurMetierException {
    public DonneeInvalideException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
