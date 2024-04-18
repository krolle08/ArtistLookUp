package Application.exception;

import org.springframework.http.HttpStatus;

public class InvalidMusicBrainzId extends RuntimeException {

    private final HttpStatus status;
    private final String message;
    public InvalidMusicBrainzId(HttpStatus status, String message) {
        super();
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
