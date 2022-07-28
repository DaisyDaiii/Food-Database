import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.model.RequestInputOffline;
import project.model.RequestOutputOffline;
import project.posts.Food;
import project.posts.Measure;
import project.posts.Nutrient;
import project.posts.Post;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class RequestOfflineTest {
    private Post post;
    private Food food;
    private Measure measure;
    private RequestInputOffline requestInputOffline;
    private RequestOutputOffline requestOutputOffline;

    @BeforeEach
    public void setup(){
        post = mock(Post.class);
        food = mock(Food.class);
        measure = mock(Measure.class);
        requestInputOffline = new RequestInputOffline();
        requestOutputOffline = new RequestOutputOffline();

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
        String ans = "Send Successfully"+"\n"+"The validation of email is checked by API, thus dummy will not show the email error";
        assertEquals(ans, requestOutputOffline.sendEmail("to", 3));
    }

    @Test
    public void searchIngredient(){
        assertNull(requestInputOffline.searchIngredient("food", "nur", "3"));
    }

    @Test
    public void addIngredient(){
        ObservableList<String> list = FXCollections.observableArrayList();
        list.add("Calories: 5.0 kcal");
        list.add("ENERC_KCAL: 0.5 kcal");
        list.add("FAT: 1 g");
        list.add("CA: 3 mg");
        list.add("CHOCDF: 0.1 g");

        assertEquals(list, requestInputOffline.addIngredient("3",food, measure, 1));
    }

    @Test
    public void nextPage(){
        assertFalse(requestInputOffline.nextPage());
    }

    @Test
    public void getFoods(){
        assertEquals(10, requestInputOffline.getFoods().size());
        assertEquals("test0", requestInputOffline.getFoods().get(0).getLabel());
    }

    @Test
    public void measure(){
        assertEquals(3, requestInputOffline.measure(food).length);
        assertEquals("whole-uri", requestInputOffline.measure(food)[0].getUri());
        assertEquals("bag", requestInputOffline.measure(food)[1].getLabel());
        assertEquals(5, requestInputOffline.measure(food)[2].getWeight());

    }

//    @Test
//    public void showAdded(){
//        assertEquals(requestOffline.showAdded().get(0), "You didn't add any ingredient");
//    }
}
