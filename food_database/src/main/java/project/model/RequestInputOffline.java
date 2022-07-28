package project.model;

import project.posts.Food;
import project.posts.Measure;

import java.util.ArrayList;
import java.util.List;

/**
 * RequestInputOffline extends Request abstract class
 * The class overrides all the abstract methods that in abstract class
 * The class will return the dummy result default
 */
public class RequestInputOffline extends RequestInput {
    /**
     * @param food the searched key word
     * @param nur the nutrition that will be set max value
     * @param num the max value
     * @return String - the searching result
     */
    @Override
    public String searchIngredient(String food, String nur, String num) {
        searchURI(food, nur, num);
        return null;
    }

    /**
     * nutrition information of added food
     * @param num the number of added food
     * @param f the added food
     * @param m the measure of added food
     * @param id the id in database of added food
     * @return the list of nutrition information of added food
     */
    @Override
    public List<String> addIngredient(String num, Food f, Measure m, int id) {
//        ObservableList<String> list = FXCollections.observableArrayList();
        List<String> list = new ArrayList<>();
        list.add("Calories: 5.0 kcal");
        list.add("ENERC_KCAL: 0.5 kcal");
        list.add("FAT: 1 g");
        list.add("CA: 3 mg");
        list.add("CHOCDF: 0.1 g");

        addCurrentNur("ENERC_KCAL", 0.5);
        addCurrentNur("FAT", 1);
        addCurrentNur("CA", 3);
        addCurrentNur("CHOCDF", 0.1);
        return list;
    }

    /**
     * change to the next page
     * @return boolean - true: success; false: already last page or error
     */
    @Override
    public boolean nextPage(){
        return false;
    }

    /**
     * change to the previous page
     * @param session current page
     */
    @Override
    public void prevPage(int session){

    }

    /**
     * @return the list of the foods of searching key word
     */
    @Override
    public List<Food> getFoods() {
//        ObservableList<Food> temp = FXCollections.observableArrayList();
        List<Food> temp = new ArrayList<>();
        for(int i=0; i<10; i++){
            Food f = new Food(i+"", "test"+i, null, null, null, null);
            temp.add(f);
        }
        return temp;
    }

    /**
     * @param f the selected food
     * @return the measures of selected food
     */
    @Override
    public Measure[] measure(Food f){
        Measure[] m = {new Measure("whole-uri", "whole", 3),
                new Measure("bag-uri", "bag", 20),
                new Measure("serving-uri", "serving", 5)};
        return m;
    }
}
