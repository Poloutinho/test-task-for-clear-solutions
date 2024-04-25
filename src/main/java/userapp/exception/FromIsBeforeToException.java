package userapp.exception;

public class FromIsBeforeToException extends RuntimeException {
    public FromIsBeforeToException(String message) {
        super(message);
    }
}
