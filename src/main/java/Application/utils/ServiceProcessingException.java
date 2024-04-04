package Application.utils;

public class ServiceProcessingException extends RuntimeException {
    public ServiceProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
