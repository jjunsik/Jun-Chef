package main.java.util.error.exception;

public class SearchErrorException extends Exception{
    public SearchErrorException() {
    }

    public SearchErrorException(String message) {
        super(message);
    }

    public SearchErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public SearchErrorException(Throwable cause) {
        super(cause);
    }
}
