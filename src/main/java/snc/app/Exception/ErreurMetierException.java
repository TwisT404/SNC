package snc.app.Exception;

import org.springframework.http.HttpStatus;

public abstract class ErreurMetierException extends RuntimeException {
    private final HttpStatus statutHttp;

    protected ErreurMetierException(String message, HttpStatus statutHttp) {
        super(message);
        this.statutHttp = statutHttp;
    }

    public HttpStatus getStatutHttp() {
        return statutHttp;
    }
}
