package project.model;

import project.posts.Food;
import project.posts.Measure;

/**
 * This class will store the information  of the added food
 */
public class SelectedFood {
    private Food food;
    private Measure measure;
    private double num;

    public SelectedFood(Food food, Measure measure, double num){
        this.food = food;
        this.measure = measure;
        this.num = num;
    }

    public Food getFood() {
        return food;
    }

    public double getNum() {
        return num;
    }

    public Measure getMeasure() {
        return measure;
    }
}
