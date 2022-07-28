package project.model;

import project.posts.Food;
import project.posts.Measure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Request class contains input API method
 * Abstract class defines several methods that will be used
 */
public abstract class RequestInput {
    private List<SelectedFood> added = new ArrayList<>();
    private  Map<String, Double> currentNur = new HashMap<>();
    private  Map<String, Integer> max = new HashMap<>();
    private int calories = 0;

    /**
     * add food
     * @param food the added food
     * @param measure measure of added food
     * @param num number of added food
     * @param cal the calories of added food
     */
    public void addFood(Food food, Measure measure, String num, double cal){
        SelectedFood sf = new SelectedFood(food, measure, Double.parseDouble(num));
        calories+=cal;
        added.add(sf);
    }

    /**
     * get the added food
     * @return the list of added food
     */
    public List<SelectedFood> getFoodList(){
        return added;
    }

    /**
     * get the total calories of all added food
     * @return int - the total calories
     */
    public int getCalories() {
        return calories;
    }

    /**
     * set max value for the assigned nutrition
     * @param nutrition the nutrition which will be set max value
     * @param num the max value
     */
    public void addMax(String nutrition, String num){
        if(max.containsKey(nutrition)){
            max.replace(nutrition, Integer.parseInt(num));
        }else{
            max.put(nutrition, Integer.parseInt(num));
        }
    }

    /**
     * @param name the name of nutrition of current food
     * @param quan the quantity of nutrition of current food
     */
    public void addCurrentNur(String name, double quan) {
        currentNur.put(name, quan);
    }

    /**
     * @return the list of nutrition of current food
     */
    public Map<String, Double> getCurrentNur() {
        return currentNur;
    }

    /**
     * @return the list of set max valur nutrition of current food
     */
    public Map<String, Integer> getMax() {
        return max;
    }

    /**
     * @param food the search key word
     * @param nur the nutrition which will be set max value
     * @param num the max value
     * @return the url of searching food for input API
     */
    public String searchURI(String food, String nur, String num){
        String[] foodList = food.split(" ");
        String text = "";
        for(int i=0; i<foodList.length; i++){
            text+=foodList[i];
            if(i != foodList.length-1){
                text+="%20";
            }
        }

        String url = "https://api.edamam.com/api/food-database/v2/parser?app_id="+System.getenv("INPUT_API_APP_ID")
                + "&app_key="+System.getenv("INPUT_API_KEY")
                + "&ingr="+text;


        if(nur != null && !num.isEmpty()){
            url+="&nutrients%5B"+nur+"%5D="+num;
            addMax(nur, num);
        }
        return url;
    }

    public abstract String searchIngredient(String food, String nur, String num);
    public abstract List<String> addIngredient(String num, Food f, Measure m, int id);
    public abstract boolean nextPage();
    public abstract void prevPage(int session);
    public abstract List<Food> getFoods();
    public abstract Measure[] measure(Food f);
}
