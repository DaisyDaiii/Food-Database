package project.posts;

public class Errors {
    private String message;
    private String field;

    public Errors(String message, String field){
        this.message = message;
        this.field = field;
    }

    public String getMessage() {
        return message;
    }

    public String getField() {
        return field;
    }
}
