package project.model;

import project.database.Database;
import project.posts.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * RequestInputOnline extends Request abstract class
 * The class overrides all the abstract methods that in abstract class
 * The class will return the results from input API
 */
public class RequestInputOnline extends RequestInput {
    private Http http;
    private Post keyPost;
    private Hints[] hints;
    private Database db;

    /**
     * @param http the class which has all methods related to http
     */
    public void setHttp(Http http){
        this.http = http;
    }

    /**
     * @return Http - get the http class
     */
    public Http getHttp(){
        return http;
    }

    /**
     * @param db the class which has database executions
     */
    public void setDb(Database db){
        this.db = db;

        db.createDB();
        db.setupDB();
    }

    /**
     * @return the database which stores the food information
     */
    public Database getDB(){
        return this.db;
    }

    /**
     * @param id the id in database of the food
     * @param num the number of stored food
     * @param f the stored food
     * @param m the measure of stored food
     * @return List<String> - the nutrition information stored in database
     */
    public List<String> cache(int id, String num, Food f, Measure m) {
        List<String> ans = getDB().getTotal(id);
        List<String> list = new ArrayList<>();
        double cal = 0;

        for(String i : ans){
            String[] info = i.split(" ");
            list.add(info[0]+": "+info[1]+" "+info[2]);
            if(info[0].equals("Calories")){
                cal = Double.parseDouble(info[1]);
            }
            addCurrentNur(info[0], Double.parseDouble(info[1]));
        }

        addFood(f, m, num, cal);

        return list;
    }

    /**
     * @param food the searched key word
     * @param nur the nutrition that will be set max value
     * @param num the max value
     * @return String - the searching result
     */
    @Override
    public String searchIngredient(String food, String nur, String num) {
        String url = searchURI(food, nur, num);
        keyPost = http.getPost(http.getRequest(url));
        if(keyPost == null){
            return ("Http request wrong");
        }else if(keyPost.getError()!=null){
            return (keyPost.getMessage());
        }

        hints = keyPost.getHints();
        return null;
    }

    /**
     * @return the list of the foods of searching key word
     */
    @Override
    public List<Food> getFoods() {
        List<Food> fs = new ArrayList<>();
        for(Hints h: hints){
            fs.add(h.getFood());
        }
        return fs;
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
        boolean add = false;
        Post post =  http.getPost(http.postRequest("https://api.edamam.com/api/food-database/v2/nutrients?app_id="+System.getenv("INPUT_API_APP_ID")+"&app_key="+System.getenv("INPUT_API_KEY"),
                Double.parseDouble(num), m.getUri(),f.getFoodId()));

        if(id == -1){
            db.addNutrients(f.getFoodId(), Double.parseDouble(num), m.getUri(), Integer.parseInt(post.getCalories()), Double.parseDouble(num));
            id = getDB().getNurID(f.getFoodId(), Double.parseDouble(num), m.getUri());
            add = true;
        }

        List<String> list = new ArrayList<>();
        double cal = Double.parseDouble(post.getCalories());
        list.add("Calories: "+cal+" kcal");
        db.addNurInfo(id, "Calories", "", cal, "kcal");

        Map<String, Nutrient> map = post.getTotalNutrients();
        for (String key : map.keySet()) {
            list.add(key + ": " + map.get(key).getQuantity()+" "+map.get(key).getUnit());
            addCurrentNur(key, map.get(key).getQuantity());

            if(add){
                db.addNurInfo(id, key, map.get(key).getLabel(), map.get(key).getQuantity(), map.get(key).getUnit());
            }
        }
        addFood(f, m, num, cal);
        return list;
    }

    /**
     * change to the next page
     * @return boolean - true: success; false: already last page or error
     */
    @Override
    public boolean nextPage(){
        if(keyPost.get_links() == null){
            return false;
        }

        keyPost = http.getPost(http.getRequest(keyPost.get_links().getNext().getHref()));
        hints = keyPost.getHints();
        return true;
    }

    /**
     * change to the previous page
     * @param session current page
     */
    @Override
    public void prevPage(int session){
        String link = keyPost.get_links().getNext().getHref();
        String[] head = link.split("\\?");
        String[] tail = head[1].split("&", 2);

        String pre = head[0]+"?session="+session+"&"+tail[1];

        keyPost = http.getPost(http.getRequest(pre));
        hints = keyPost.getHints();
    }

    /**
     * @param f the selected food
     * @return the measures of selected food
     */
    @Override
    public Measure[] measure(Food f){
        Measure[] m = null;

        for(Hints h: hints){
            if(h.getFood() == f){
                m = h.getMeasures();
                break;
            }
        }

        return m;
    }
}
