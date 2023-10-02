package main.java.util.error.junchef;

public class JunChefException extends RuntimeException{
    private final int code;
    private final String title;
    private final String message;

    public JunChefException(int code, String title, String message) {
        super(message);
        this.code = code;
        this.title = title;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
