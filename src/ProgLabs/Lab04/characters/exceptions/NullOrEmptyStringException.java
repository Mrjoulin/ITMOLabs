package Lab04.characters.exceptions;

public class NullOrEmptyStringException extends RuntimeException {
    public NullOrEmptyStringException(String message) {
        super(message);
    }
}
