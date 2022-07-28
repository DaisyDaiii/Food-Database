package project.presenter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.chart.XYChart;
import project.model.GameModel;
import project.model.SelectedFood;
import project.posts.Food;
import project.posts.Measure;
import project.view.MainController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Presenter class will get the value of information from model
 * then will set the information to the GUI
 */
public class Presenter {
    GameModel model;
    String inputStatus;
    String outputStatus;
    MainController view;

    private Search searchConcurrency = new Search();
    private EmailOutput emailOutput = new EmailOutput();
    private OnlineAddIngredient onlineAddIngredient = new OnlineAddIngredient();
    private OfflineAddIngredient offlineAddIngredient = new OfflineAddIngredient();
    private OnlineNextPage onlineNextPage = new OnlineNextPage();
    private OnlinePrevPage onlinePrevPage = new OnlinePrevPage();

    public Presenter(GameModel model, String inputStatus, String outputStatus){
        this.model = model;
        this.inputStatus = inputStatus;
        this.outputStatus = outputStatus;
    }

    public void setView(MainController view){
        this.view = view;
    }

    public String getOutputStatus() {
        return outputStatus;
    }

    /*
    ------------------------------------------------------------------------------------------------
    MODEL FUNCTIONS
    ------------------------------------------------------------------------------------------------
     */


    /**
     * clear the current added nutrient information
     */
    public void clearCurrentNur(){
        if(inputStatus.equals("offline")){
            model.getCurrentNurOffline().clear();
        }else{
            model.getCurrentNurOnline().clear();
        }
    }

    /**
     * get the measures of the selected food online
     * @param f the selected food
     * @return the measures of the selected food
     */
    public Measure[] getMeasure(Food f){
        if(inputStatus.equals("offline")){
            return model.measureOffline(f);
        }
        return model.measureOnline(f);
    }

    /**
     * get the ID of a nutrition in database
     * @param f the food
     * @param num the number of food
     * @param m the measure of food
     * @return the id of searched food in database
     */
    public int getNurID(Food f, String num, Measure m){
        return model.getNurID(f, num, m);
    }

    /**
     * get the information of added food
     * @return the list of added food
     */
    public ObservableList<String> showAdded(){
        ObservableList<String> items = FXCollections.observableArrayList();
        items.clear();

        List<SelectedFood> added= null;

        if(inputStatus.equals("offline")){
            added = model.getSelectedFoodOffline();
        }else{
            added = model.getSelectedFoodOnline();
        }

        if(added.size() == 0){
            items.add("You didn't add any ingredient");
        }else{
            for(SelectedFood i: added){
                items.add("Food: "+i.getFood().getLabel()+"\n"
                        +"Measure: "+i.getMeasure().getLabel()+"\n"
                        +"Quantity: "+model.quantity(i.getNum()));
            }
        }
        return items;
    }

    /**
     * get the nutrition which has max value
     * @param series1 the max value
     * @param series2 the actual value
     * @return the list of nutrition which has max value
     */
    public List<String> bar(XYChart.Series<String, Number> series1, XYChart.Series<String, Number> series2){
        Map<String, Integer> max = null;
        Map<String, Double> nur = null;

        if(inputStatus.equals("offline")){
//            max = model.getMaxOffline();
            max = model.maximums(model.getMaxOffline());
            nur = model.getCurrentNurOffline();
        }else{
//            max = model.getMaxOnline();
            max = model.maximums(model.getMaxOnline());
            nur = model.getCurrentNurOnline();
        }

        List<String> name = new ArrayList<>();
        for(String i : max.keySet()){
            if(nur.containsKey(i)){
                name.add(i);
                series1.getData().add(new XYChart.Data<>(i, max.get(i)));
                series2.getData().add(new XYChart.Data<>(i, nur.get(i)));
            }
        }

        return name;
    }

    /**
     * create a new database
     */
    public void newDB(){
        model.removeDB();
        model.createDB();
        model.setupDB();
    }

    /**
     * search foods with key word
     * @param food the searched key word
     * @param nur the nutrition that will be set max value
     * @param num the setted max value
     * @return the string of success or error message
     */
    public String searchIngredient(String food, String nur, String num){
        if(inputStatus.equals("offline")){
            return model.searchIngredientOffline(food, nur, num);
        }else{
            return model.searchIngredientOnline(food, nur, num);
        }
    }

    /**
     * get all foods of the searched nutrition
     * @return all foods of the searched nutrition
     */
    public ObservableList<Food> getFoods(){
        ObservableList<Food> fs = FXCollections.observableArrayList();
        List<Food> f = new ArrayList<>();
        if(inputStatus.equals("offline")){
            f =  model.getFoodsOffline();
        }else{
            f =  model.getFoodsOnline();
        }
        for(Food food: f){
            fs.add(food);
        }
        return fs;
    }

    /**
     * send the email
     * @param target the receiver email
     * @return String - the showing text after send email
     */
    public String sendEmail(String target){
        if(outputStatus.equals("offline")){
            return model.sendEmailOffline(target, model.getModeNumber());
        }else{
            return model.sendEmailOnline(target, model.getModeNumber());
        }
    }

    /**
     * get the food list of next page offline
     * @return the food list of next page
     */
    public ObservableList<Food> nextPageOnline(){
        ObservableList<Food> fs = FXCollections.observableArrayList();
        for(Food f: model.nextPageOnline()){
            fs.add(f);
        }
        return fs;
    }

    /**
     * get the foods of previous page online
     * @param session the page number of current page
     * @return the foods of previous page
     */
    public ObservableList<Food> prevPageOnline(int session){
        ObservableList<Food> fs = FXCollections.observableArrayList();
        for(Food f: model.prevPageOnline(session)){
            fs.add(f);
        }
        return fs;
    }

    /**
     * get the nutrient information of the food
     * @param num number of the food
     * @param f the added food
     * @param m measure of added food
     * @param id the id in database of added food
     * @return the nutrient information of the food
     */
    public ObservableList<String> addIngredient(String num, Food f, Measure m, int id){
        ObservableList<String> list = FXCollections.observableArrayList();
        List<String> l = new ArrayList<>();
        if(inputStatus.equals("offline")){
//            l =  model.addIngredientOffline(num, f, m, id);
            l = model.nutrition(model.addIngredientOffline(num, f, m, id));
        }else{
//            l =  model.addIngredientOnline(num, f, m, id);
            l = model.nutrition(model.addIngredientOnline(num, f, m, id));
        }
        for(String s: l){
            list.add(s);
        }
        return list;
    }

    /**
     * add the selected food
     * @param food the added food
     * @param measure measure of added food
     * @param num number of added food
     * @param cal calories of added food
     */
    public void addFood(Food food, Measure measure, String num, int cal){
        if(inputStatus.equals("offline")){
            model.addFoodOffline(food, measure, num, cal);
        }else{
            model.addFoodOnline(food, measure, num, cal);
        }
    }

    /**
     * get the information from database
     * @param id the id of the food
     * @param num number of the food
     * @param f food name
     * @param m measure of the food
     * @return List<String> - the nutrition information stored in database
     */
    public ObservableList<String> cache(int id, String num, Food f, Measure m){
        List<String> l = model.nutrition(model.cache(id, num, f, m));
        ObservableList<String> list = FXCollections.observableArrayList();
        for(String s: l){
            list.add(s);
        }
        return list;
    }

    /*
    ------------------------------------------------------------------------------------------------
    VIEW FUNCTIONS
    ------------------------------------------------------------------------------------------------
     */

    /**
     * when search button clicked, search information and display it on the GUI
     * @param food search food
     * @param nur set nutrition
     * @param num max value
     */
    public void SearchConcurrency(String food, String nur, String num) {
        searchConcurrency.food = food;
        searchConcurrency.nur = nur;
        searchConcurrency.num = num;
        searchConcurrency.restart();
    }

    /**
     * when send email button clicked, try to send email and display the result on the GUI
     * @param to the receiver
     */
    public void EmailConcurrency(String to) {
        emailOutput.to = to;
        emailOutput.restart();
    }

    /**
     * when add ingredient button clicked offline, add it and display the information on the GUI
     * @param f added food
     * @param m measure of added food
     * @param num number of added food
     */
    public void OfflineAddIngredientConcurrency(Food f, Measure m, String num) {
        offlineAddIngredient.f = f;
        offlineAddIngredient.m = m;
        offlineAddIngredient.num = num;
        offlineAddIngredient.restart();
    }

    /**
     * when add ingredient button clicked online, add it and display the information on the GUI
     * @param f added food
     * @param m measure of added food
     * @param num number of added food
     * @param refresh whether get it from API or cache
     */
    public void OnlineAddIngredientConcurrency(Food f, Measure m, String num, boolean refresh) {
        onlineAddIngredient.f = f;
        onlineAddIngredient.m = m;
        onlineAddIngredient.num = num;
        onlineAddIngredient.refresh = refresh;
        onlineAddIngredient.restart();
    }

    /**
     * when next button clicked online,  search information and display it on the GUI
     */
    public void OnlineNextPageConcurrency() {
        onlineNextPage.restart();
    }

    /**
     * when previous button clicked online,  search information and display it on the GUI
     * @param session
     */
    public void OnlinePrevPageConcurrency(int session) {
        onlinePrevPage.session = session;
        onlinePrevPage.restart();
    }

    /*
    ------------------------------------------------------------------------------------------------
    CONCURRENCY FUNCTIONS
    ------------------------------------------------------------------------------------------------
     */

    private class Search extends Service {
        private String food;
        private String nur;
        private String num;

        /**
         * Overrides createTask for own usage
         * @return a concurrent task
         */
        @Override
        protected Task createTask() {
            return new Task() {

                /**
                 * search the ingredient online
                 * @return Post that returned by model
                 * @throws Exception
                 */
                @Override
                protected String call() throws Exception {
                    return searchIngredient(food, nur, num);
                }

                /**
                 * refresh the GUI to display information
                 */
                @Override
                protected void succeeded() {
                    view.refreshSearch((String) getValue());
                }
            };
        }
    }

    private class OnlineNextPage extends Service {

        /**
         * Overrides createTask for own usage
         * @return a concurrent task
         */
        @Override
        protected Task createTask() {
            return new Task() {

                /**
                 * get the foods of next page online
                 * @return Post that returned by model
                 * @throws Exception
                 */
                @Override
                protected Void call() throws Exception {
                    Thread.sleep(200);
                    return null;
                }

                /**
                 * refresh the GUI to display information
                 */
                @Override
                protected void succeeded() {
                    ObservableList<Food> list = nextPageOnline();
                    view.refreshNext(list);
                }
            };
        }
    }

    private class OnlinePrevPage extends Service {
        private int session;

        /**
         * Overrides createTask for own usage
         * @return a concurrent task
         */
        @Override
        protected Task createTask() {
            return new Task() {

                /**
                 * get the foods of previous page online
                 * @return Post that returned by model
                 * @throws Exception
                 */
                @Override
                protected Void call() throws Exception {
                    Thread.sleep(200);
                    return null;
                }

                /**
                 * refresh the GUI to display information
                 */
                @Override
                protected void succeeded() {
                    ObservableList<Food> list = prevPageOnline(session);
                    view.refreshPrev(list);
                }
            };
        }
    }

    private class OnlineAddIngredient extends Service {
        private String num;
        private Food f;
        private Measure m;
        private boolean refresh;

        /**
         * Overrides createTask for own usage
         * @return a concurrent task
         */
        @Override
        protected Task createTask() {
            return new Task() {

                /**
                 * add the ingredient online
                 * @return Post that returned by model
                 * @throws Exception
                 */
                @Override
                protected Integer call() throws Exception {
                    Thread.sleep(200);
                    return getNurID(f, num, m);
                }

                /**
                 * refresh the GUI to display information
                 */
                @Override
                protected void succeeded() {
                    ObservableList<String> list = FXCollections.observableArrayList();

                    if(!refresh){
                        list = cache((int) getValue(), num, f, m);
                    }else{
                        list = addIngredient(num, f, m, (int) getValue());
                    }

                    view.refreshOnlineIngredient(list);
                }
            };
        }
    }

    private class OfflineAddIngredient extends Service {
        private String num;
        private Food f;
        private Measure m;

        /**
         * Overrides createTask for own usage
         * @return a concurrent task
         */
        @Override
        protected Task createTask() {
            return new Task() {

                /**
                 * add the ingredient offline
                 * @return JSONObject that returned by model
                 * @throws Exception
                 */
                @Override
                protected Void call() throws Exception {
                    Thread.sleep(1000);

                    addFood(f, m, num, 3);
                    return null;
                }

                /**
                 * refresh the GUI to display information
                 */
                @Override
                protected void succeeded() {
                    view.refreshOfflineIngredient(addIngredient(null, null, null, 0));
                }
            };
        }
    }

    private class EmailOutput extends Service {
        private String to;

        /**
         * Overrides createTask for own usage
         * @return a concurrent task
         */
        @Override
        protected Task createTask() {
            return new Task() {

                /**
                 * send the report to receiver via email online
                 * @return Post that returned by model
                 * @throws Exception
                 */
                @Override
                protected String call() throws Exception {
                    return sendEmail(to);
                }

                /**
                 * refresh the GUI to display information
                 */
                @Override
                protected void succeeded() {
                    view.refreshEmail((String) getValue());
                }
            };
        }
    }

    /*
    ------------------------------------------------------------------------------------------------
    FEATURE EXTENSION
    ------------------------------------------------------------------------------------------------
     */

    /**
     * update the mode
     * @param mode the chosen mode
     */
    public void setMode(String mode){
        model.setMode(mode);
    }

    /**
     * get the current mode name
     * @return String - ucurrent mode name
     */
    public String getModeName(){
        return model.getModeName();
    }

    /**
     * set the number of meals of current mode
     * @param number the number of meals
     */
    public void setModeNumber(int number){
        model.setModeNumber(number);
    }

    /**
     * @return int - the number of meals of current mode
     */
    public int getModeNumber(){
        return model.getModeNumber();
    }
}
