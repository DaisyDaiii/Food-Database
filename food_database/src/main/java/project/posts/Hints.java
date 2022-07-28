package project.posts;

public class Hints {
    private Food food;
    private Measure[] measures;

    public Hints(Food food, Measure[] measures){
        this.food = food;
        this.measures = measures;
    }

    public Food getFood() {
        return food;
    }

    public Measure[] getMeasures() {
        return measures;
    }
}
