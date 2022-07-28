package project.posts;

public class Ingredients {
    private double quantity;
    private String measureURI;
    private String foodId;

    public Ingredients(double quantity, String measureURI, String foodId){
        this.quantity = quantity;
        this.measureURI = measureURI;
        this.foodId = foodId;
    }

    public String getFoodId() {
        return foodId;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getMeasureURI() {
        return measureURI;
    }
}
