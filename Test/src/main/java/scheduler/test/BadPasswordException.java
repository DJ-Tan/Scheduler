package scheduler.test;

public class BadPasswordException extends IllegalArgumentException{

    public BadPasswordException(String message) {
        super(message);
    }
}
