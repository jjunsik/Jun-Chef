package main.java.util.error;

public class ErrorFormat {
    private final String title;
    private final String message;

    public ErrorFormat(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }
}
