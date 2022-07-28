package project.posts;

import java.util.Map;

public class Post {
    private String text;
    private Hints[] hints;
    private String error;
    private String message;
    private Map<String, Nutrient> totalNutrients;
    private Links _links;
    private String calories;
    private Errors[] errors;

    public Post(String text, Hints[] hints,
                String error, String message,
                Map<String, Nutrient> totalNutrients, Links _links,
                String calories, Errors[] errors){
        this.text = text;
        this.hints = hints;
        this.error = error;
        this.message = message;
        this.totalNutrients = totalNutrients;
        this._links = _links;
        this.calories = calories;
        this.errors = errors;
    }

    public Hints[] getHints() {
        return hints;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getText() {
        return text;
    }

    public Map<String, Nutrient> getTotalNutrients() {
        return totalNutrients;
    }

    public Links get_links() {
        return _links;
    }

    public String getCalories() {
        return calories;
    }

    public Errors[] getErrors() {
        return errors;
    }

}
