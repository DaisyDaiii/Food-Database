package project.posts;

public class Measure {
    private String uri;
    private String label;
    private double weight;

    public Measure(String uri, String label, double weight){
        this.uri = uri;
        this.label = label;
        this.weight = weight;
    }

    public String getLabel() {
        return label;
    }

    public double getWeight() {
        return weight;
    }

    public String getUri() {
        return uri;
    }

    @Override
    public String toString(){
        return label;
    }
}
