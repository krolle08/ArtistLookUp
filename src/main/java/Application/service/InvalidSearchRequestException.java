package Application.service;

public class InvalidSearchRequestException extends  Exception{
    public InvalidSearchRequestException(String message) {
        super(message);
    }

}
