package project.posts;

public class Nutrient {
    private String label;
    private double quantity;
    private String unit;

    public Nutrient(String label, double quantity, String unit){
        this.label = label;
        this.quantity = quantity;
        this.unit = unit;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getLabel() {
        return label;
    }

    public String getUnit() {
        return unit;
    }
}
