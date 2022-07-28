package project.model;

import project.database.Database;
import project.model.mode.MealPrepMode;
import project.model.mode.Mode;
import project.model.mode.NormalMode;
import project.posts.Food;
import project.posts.Measure;

import java.util.List;
import java.util.Map;

/**
 * The model will call two Request for offline and online version
 * The model will call Database and Http to execute some operations
 */
public class GameModel {
    RequestInputOnline requestOnline;
    RequestInputOffline requestOffline;
    RequestOutputOnline requestOutputOnline;
    RequestOutputOffline requestOutputOffline;
    Http http;
    Database db;
    Mode mode;

    private Mode normal = new NormalMode("Normal", 1);
    private Mode meal = new MealPrepMode("Meal prep", 1);

    /**
     * constructor of GameModel
     * @param requestOnline an object of RequestOnline class
     * @param requestOffline an object of RequestOffline class
     */
    public GameModel(RequestInputOnline requestOnline, RequestInputOffline requestOffline, RequestOutputOnline requestOutputOnline, RequestOutputOffline requestOutputOffline){
        this.requestOnline = requestOnline;
        this.requestOffline = requestOffline;
        this.requestOutputOnline = requestOutputOnline;
        this.requestOutputOffline = requestOutputOffline;
        this.mode = normal;

        http = new Http();
        db = new Database();

        requestOnline.setDb(db);
        requestOnline.setHttp(http);
    }

    /**
     * remove the database
     * @return boolean - whether the database is deleted
     */
    public boolean removeDB(){
        return requestOnline.getDB().removeDB();
    }

    /**
     * create the database
     * @return boolean - whether the database is created
     */
    public boolean createDB(){
        return requestOnline.getDB().createDB();
    }

    /**
     * initial the database
     * @return boolean - whether the tables in database is created
     */
    public boolean setupDB(){
        return requestOnline.getDB().setupDB();
    }

    /**
     * get the ID of a nutrition in database
     * @param f the food
     * @param num the number of food
     * @param m the measure of food
     * @return the id of searched food in database
     */
    public int getNurID(Food f, String num, Measure m){
        return requestOnline.getDB().getNurID(f.getFoodId(), Double.parseDouble(num), m.getUri());
    }

    /**
     * get the current added nutrient online
     * @return the current added nutrient information
     */
    public Map<String, Double> getCurrentNurOnline(){
        return requestOnline.getCurrentNur();
    }

    /**
     * get the current added nutrient offline
     * @return the current added nutrient information
     */
    public Map<String, Double> getCurrentNurOffline(){
        return requestOffline.getCurrentNur();
    }

    /**
     * send the email online
     * @param target the receiver email
     * @param number The number of meals of current mode
     * @return String - the showing text after send email
     */
    public String sendEmailOnline(String target, int number){
        return requestOutputOnline.sendEmail(target, number);
    }

    /**
     * send the email offline
     * @param target the receiver email
     * @return String - the showing text after send email
     */
    public String sendEmailOffline(String target, int number){
        return requestOutputOffline.sendEmail(target, number);
    }

    /**
     * get the information from database
     * @param id the id of the food
     * @param num number of the food
     * @param f food name
     * @param m measure of the food
     * @return List<String> - the nutrition information stored in database
     */
    public List<String> cache(int id, String num, Food f, Measure m){
        return requestOnline.cache(id, num, f, m);
    }

    /**
     * get the nutrient information which has been set max value online
     * @return
     */
    public Map<String, Integer> getMaxOnline(){
        return requestOnline.getMax();
    }

    /**
     * get the nutrient information which has been set max value offline
     * @return
     */
    public Map<String, Integer> getMaxOffline(){
        return requestOffline.getMax();
    }

    /**
     * get the nutrient information of the food online
     * @param num number of the food
     * @param f the added food
     * @param m measure of added food
     * @param id the id in database of added food
     * @return the nutrient information of the food
     */
    public List<String> addIngredientOnline(String num, Food f, Measure m, int id){
        return requestOnline.addIngredient(num, f, m, id);
    }

    /**
     * get the nutrient information of the food offline
     * @param num number of the food
     * @param f the added food
     * @param m measure of added food
     * @param id the id in database of added food
     * @return the nutrient information of the food
     */
    public List<String> addIngredientOffline(String num, Food f, Measure m, int id){
        return requestOffline.addIngredient(num, f, m, id);
    }

    /**
     * get all foods of the searched nutrition online
     * @return all foods of the searched nutrition
     */
    public List<Food> getFoodsOnline(){
        return requestOnline.getFoods();
    }

    /**
     * get all foods of the searched nutrition offline
     * @return all foods of the searched nutrition
     */
    public List<Food> getFoodsOffline(){
        return requestOffline.getFoods();
    }

    /**
     * get the measures of the selected food online
     * @param f the selected food
     * @return the measures of the selected food
     */
    public Measure[] measureOnline(Food f){
        return requestOnline.measure(f);
    }

    /**
     * get the measures of the selected food offline
     * @param f the selected food
     * @return the measures of the selected food
     */
    public Measure[] measureOffline(Food f){
        return requestOffline.measure(f);
    }

    /**
     * add the selected food online
     * @param food the added food
     * @param measure measure of added food
     * @param num number of added food
     * @param cal calories of added food
     */
    public void addFoodOnline(Food food, Measure measure, String num, int cal){
        requestOnline.addFood(food, measure, num, cal);
    }

    /**
     * add the selected food offline
     * @param food the added food
     * @param measure measure of added food
     * @param num number of added food
     * @param cal calories of added food
     */
    public void addFoodOffline(Food food, Measure measure, String num, int cal){
        requestOffline.addFood(food, measure, num, cal);
    }

    /**
     * get the total selected food online
     * @return the list of total selected food
     */
    public List<SelectedFood> getSelectedFoodOnline(){
        return requestOnline.getFoodList();
    }

    /**
     * get the total selected food offline
     * @return the list of total selected food
     */
    public List<SelectedFood> getSelectedFoodOffline(){
        return requestOffline.getFoodList();
    }

    /**
     * search foods with key word online
     * @param food the searched key word
     * @param nur the nutrition that will be set max value
     * @param num the setted max value
     * @return the string of success or error message
     */
    public String searchIngredientOnline(String food, String nur, String num){
        return requestOnline.searchIngredient(food, nur, num);
    }

    /**
     * search foods with key word offline
     * @param food the searched key word
     * @param nur the nutrition that will be set max value
     * @param num the set max value
     * @return the string of success or error message
     */
    public String searchIngredientOffline(String food, String nur, String num){
        return requestOffline.searchIngredient(food, nur, num);
    }

    /**
     * get the food list of next page online
     * @return the food list of next page
     */
    public List<Food> nextPageOnline(){
        if(!requestOnline.nextPage()){
            return null;
        }
        return requestOnline.getFoods();
    }

    /**
     * get the food list of next page offline
     * @return the food list of next page
     */
    public boolean nextPageOffline(){
        return requestOffline.nextPage();
    }

    /**
     * get the foods of previous page online
     * @param session the page number of current page
     * @return the foods of previous page
     */
    public List<Food> prevPageOnline(int session){
        requestOnline.prevPage(session);
        return requestOnline.getFoods();
    }

    /**
     * get the foods of previous page offline
     * @param session the page number of current page
     * @return the foods of previous page
     */
    public void prevPageOffline(int session){
        requestOffline.prevPage(session);
    }

    /*
    ------------------------------------------------------------------------------------------------
    FEATURE EXTENSION
    ------------------------------------------------------------------------------------------------
     */

    /**
     * get the maximums in current mode
     * @param max the original map of all nutrition that set max value
     * @return
     */
    public Map<String, Integer> maximums(Map<String, Integer> max){
        return mode.maximums(max);
    }

    /**
     * get the nutrition in current mode
     * @param nur the original nutrition information
     * @return
     */
    public List<String> nutrition(List<String> nur){
        return mode.nutrition(nur);
    }

    /**
     * get the quantity in current mode
     * @param num the original food quantity
     * @return
     */
    public double quantity(double num){
        return mode.quantity(num);
    }

    /**
     * update the mode
     * @param mode the chosen mode
     */
    public void setMode(String mode){
        if(mode.equals("Normal")){
            this.mode = normal;
        }else if(mode.equals("Meal prep")){
            this.mode = meal;
        }
    }

    /**
     * @return Steing - the name of current mode
     */
    public String getModeName(){
        return this.mode.getName();
    }

    /**
     * update the number of meals of current mode
     * @param number
     */
    public void setModeNumber(int number){
        this.mode.setNumber(number);
    }

    /**
     * @return int - the number of meals of current mode
     */
    public int getModeNumber(){
        return mode.getNumber();
    }
}
