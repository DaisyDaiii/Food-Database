import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.database.Database;
import project.model.Http;
import project.model.RequestInputOnline;
import project.model.RequestOutputOnline;
import project.posts.Food;
import project.posts.Measure;
import project.posts.Nutrient;
import project.posts.Post;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class RequestOnlineTest {
    private Http http;
    private Post post;
    private Food food;
    private Measure measure;
    private Database db;
    private RequestInputOnline requestInputOnline;
    private RequestOutputOnline requestOutputOnline;


    @BeforeEach
    public void setup(){
        http = mock(Http.class);
        post = mock(Post.class);
        food = mock(Food.class);
        db = mock(Database.class);
        measure = mock(Measure.class);
        requestInputOnline = new RequestInputOnline();
        requestOutputOnline = new RequestOutputOnline(requestInputOnline);

        requestInputOnline.setHttp(http);
        requestInputOnline.setDb(db);

        Map<String, Nutrient> map = new HashMap<>();
        map.put("nur1", new Nutrient("label1", 2, "u1"));
        map.put("nur2", new Nutrient("label2", 3, "u2"));
        map.put("nur3", new Nutrient("label3", 4, "u3"));
        map.put("nur4", new Nutrient("label4", 5, "u4"));

        when(post.getTotalNutrients()).thenReturn(map);
        when(post.getCalories()).thenReturn(10+"");

        when(food.getFoodId()).thenReturn("foodID");
        when(food.getLabel()).thenReturn("foodLabel");
        when(measure.getLabel()).thenReturn("measureLabel");
        when(measure.getUri()).thenReturn("measureURI");
    }

    @Test
    public void sendEmail(){
        when(http.getPost(http.emailRequest(System.getenv("SENDGRID_API_EMAIL"), "target",
                System.getenv("SENDGRID_API_KEY"),
                requestOutputOnline.content(3)))).thenReturn(post);

        assertEquals(requestOutputOnline.sendEmail("target", 3), "Send Successfully");
    }

    @Test
    public void searchIngredient(){
        String url = "https://api.edamam.com/api/food-database/v2/parser?app_id="+System.getenv("INPUT_API_APP_ID")
                + "&app_key="+System.getenv("INPUT_API_KEY")
                + "&ingr="+"text";

        when(http.getRequest(url)).thenReturn(null);
        when(http.getPost(null)).thenReturn(post);
        when(post.getError()).thenReturn(null);
        assertNull(requestInputOnline.searchIngredient("text", null, ""));
    }

    @Test
    public void setMaxTest(){
        requestInputOnline.addMax("haha", "3");
        assertEquals(requestInputOnline.getMax().get("haha"), 3);

        requestInputOnline.addMax("haha", "5");
        assertEquals(requestInputOnline.getMax().get("haha"), 5);

        assertEquals(requestInputOnline.getMax().size(), 1);

        requestInputOnline.addMax("test", "10");
        assertEquals(requestInputOnline.getMax().size(), 2);
    }

    @Test
    public void addedIngredientsTest(){
        requestInputOnline.addFood(food, measure, "3", Integer.parseInt(post.getCalories()));
        assertEquals(requestInputOnline.getFoodList().size(), 1);
        assertEquals(requestInputOnline.getFoodList().get(0).getFood().getLabel(), "foodLabel");
        assertEquals(requestInputOnline.getCalories(), 10);

        requestInputOnline.addFood(food, measure, "3", Integer.parseInt(post.getCalories()));
        assertEquals(requestInputOnline.getFoodList().size(), 2);
        assertEquals(requestInputOnline.getFoodList().get(1).getFood().getLabel(), "foodLabel");
        assertEquals(requestInputOnline.getCalories(), 20);
    }

    @Test
    public void CurrentNutritionTest(){
        Map<String, Nutrient> nur = post.getTotalNutrients();
        for(String name: nur.keySet()){
            requestInputOnline.addCurrentNur(name, nur.get(name).getQuantity());
        }

        assertEquals(requestInputOnline.getCurrentNur().size(), 4);
    }

    @Test
    public void ContentTest(){
        assertEquals(requestOutputOnline.content(3), "You didn't add any ingredient");

        requestInputOnline.addFood(food, measure, "3", Integer.parseInt(post.getCalories()));

        String content = "";
        content+="Total calories: "+30+""+"\n";
        content+="\n";
        content+="Food: "+"foodLabel"+"\n"
                +"Measure: "+"measureLabel"+"\n"
                +"Quantity: "+9.0+"\n";
        content+="\n";

        assertEquals(requestOutputOnline.content(3), content);
    }


    @Test
    public void addIngredient(){
        Map<String, Nutrient> map2 = new HashMap<>();
        map2.put("nur1", new Nutrient("label1", 2, "u1"));
        map2.put("nur2", new Nutrient("label2", 3, "u2"));

        when(http.getPost(http.postRequest("https://api.edamam.com/api/food-database/v2/nutrients?app_id="+System.getenv("INPUT_API_APP_ID")+"&app_key="+System.getenv("INPUT_API_KEY"),
                Double.parseDouble("3"), measure.getUri(),food.getFoodId()))).thenReturn(post);
        when(db.getNurID(food.getFoodId(), Double.parseDouble("3"), measure.getUri())).thenReturn(1);
        when(post.getTotalNutrients()).thenReturn(map2);
        ObservableList<String> list = FXCollections.observableArrayList();
        list.add("Calories: "+10.0+" kcal");
        list.add("nur2" + ": " + 3.0+" u2");
        list.add("nur1" + ": " + 2.0+" u1");
        assertEquals(list, requestInputOnline.addIngredient("3", food, measure, -1));
    }
}
