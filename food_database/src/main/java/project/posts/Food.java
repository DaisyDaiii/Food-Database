package project.posts;

import java.util.HashMap;
import java.util.Map;

public class Food {
    private String foodId;
    private String label;
    private Map<String, Double> nutrients = new HashMap<>();
    private String category;
    private String categoryLabel;
    private String foodContentsLabel;

    public Food(String foodId, String label,
                Map<String, Double> nutrients, String category,
                String categoryLabel, String foodContentsLabel){
        this.foodId = foodId;
        this.label = label;
        this.nutrients = nutrients;
        this.category = category;
        this.categoryLabel = categoryLabel;
        this.foodContentsLabel = foodContentsLabel;
    }

    public Map<String, Double> getNutrients() {
        return nutrients;
    }

    public String getFoodId() {
        return foodId;
    }

    public String getLabel() {
        return label;
    }

    public String getCategory() {
        return category;
    }

    public String getCategoryLabel() {
        return categoryLabel;
    }

    public String getFoodContentsLabel() {
        return foodContentsLabel;
    }

    @Override
    public String toString(){
        return label;
    }
}
