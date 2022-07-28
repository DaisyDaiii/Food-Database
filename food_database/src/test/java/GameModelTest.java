import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.database.Database;
import project.model.*;
import project.posts.Food;
import project.posts.Measure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class GameModelTest {
    private GameModel model;
    private Food food;
    private Measure measure;
    private Database db;
    private RequestInputOffline requestInputOffline;
    private RequestInputOnline requestInputOnline;
    private RequestOutputOnline requestOutputOnline;
    private RequestOutputOffline requestOutputOffline;


    @BeforeEach
    public void setup(){
        food = mock(Food.class);
        db = mock(Database.class);
        measure = mock(Measure.class);
        requestInputOffline = mock(RequestInputOffline.class);
        requestInputOnline = mock(RequestInputOnline.class);
        requestOutputOffline = mock(RequestOutputOffline.class);
        requestOutputOnline = mock(RequestOutputOnline.class);
        model = new GameModel(requestInputOnline, requestInputOffline, requestOutputOnline, requestOutputOffline);
    }

    @Test
    public void searchIngredientOnlineTest(){
        when(requestInputOnline.searchIngredient("food", "nurtrition", "3")).thenReturn(null);
        assertNull(model.searchIngredientOnline("food", "nurtrition", "3"));
    }

    @Test
    public void sendEmailOnlineTest(){
        when(requestOutputOnline.sendEmail("to", 3)).thenReturn("Send Successfully");
        assertEquals(model.sendEmailOnline("to", 3), "Send Successfully");
    }

    @Test
    public void addIngredientOnline(){
        ObservableList<String> list = FXCollections.observableArrayList();
        list.add("test1: 1g");
        list.add("test2: 2g");
        list.add("test3: 3g");

        when(requestInputOnline.addIngredient("3", food, measure, 1)).thenReturn(list);
        assertEquals(list, model.addIngredientOnline("3", food, measure, 1));
    }

    @Test
    public void nextPageOnline(){
        ObservableList<Food> list = FXCollections.observableArrayList();
        list.add(food);

        when(requestInputOnline.nextPage()).thenReturn(false);
        assertNull(model.nextPageOnline());

        when(requestInputOnline.nextPage()).thenReturn(true);
        when(requestInputOnline.getFoods()).thenReturn(list);
        assertEquals(list, model.nextPageOnline());
    }

    @Test
    public void prevPageOnline(){
        ObservableList<Food> list = FXCollections.observableArrayList();
        list.add(food);

        when(requestInputOnline.getFoods()).thenReturn(list);
        assertEquals(list, model.prevPageOnline(10));
    }

    @Test
    public void getFoodsOnline(){
        ObservableList<Food> list = FXCollections.observableArrayList();
        list.add(food);

        when(requestInputOnline.getFoods()).thenReturn(list);
        assertEquals(list, model.getFoodsOnline());
    }

    @Test
    public void measureOnline(){
        Measure[] m = new Measure[1];
        m[0] = measure;

        when(requestInputOnline.measure(food)).thenReturn(m);
        assertEquals(m, model.measureOnline(food));
    }

    @Test
    public void removeDB(){
        when(requestInputOnline.getDB()).thenReturn(db);
        when(db.removeDB()).thenReturn(true);

        assertTrue(model.removeDB());
    }

    @Test
    public void createDB(){
        when(requestInputOnline.getDB()).thenReturn(db);
        when(db.createDB()).thenReturn(true);

        assertTrue(model.createDB());
    }

    @Test
    public void setupDB(){
        when(requestInputOnline.getDB()).thenReturn(db);
        when(db.setupDB()).thenReturn(true);

        assertTrue(model.setupDB());
    }

    @Test
    public void getNurID(){
        when(requestInputOnline.getDB()).thenReturn(db);
        when(db.getNurID("food", 3, "measure")).thenReturn(1);

        when(food.getFoodId()).thenReturn("food");
        when(measure.getUri()).thenReturn("measure");
        assertEquals(model.getNurID(food, "3", measure), 1);
    }

    @Test
    public void getCurrentNurOnline(){
        Map<String, Double> map = new HashMap<>();
        map.put("test", 3.0);

        when(requestInputOnline.getCurrentNur()).thenReturn(map);
        assertEquals(map, model.getCurrentNurOnline());
    }

    @Test
    public void getCurrentNurOffline(){
        Map<String, Double> map = new HashMap<>();
        map.put("test", 3.0);

        when(requestInputOffline.getCurrentNur()).thenReturn(map);
        assertEquals(map, model.getCurrentNurOffline());
    }

    @Test
    public void sendEmailOffline(){
        String ans = "Send Successfully"+"\n"+"The validation of email is checked by API, thus dummy will not show the email error";

        when(requestOutputOffline.sendEmail("to", 3)).thenReturn(ans);
        assertEquals(ans, model.sendEmailOffline("to", 3));
    }

    @Test
    public void addIngredientOffline(){
        ObservableList<String> list = FXCollections.observableArrayList();
        list.add("test1: 1g");
        list.add("test2: 2g");
        list.add("test3: 3g");

        when(requestInputOffline.addIngredient("3", food, measure, 1)).thenReturn(list);
        assertEquals(list, model.addIngredientOffline("3", food, measure, 1));
    }

    @Test
    public void getFoodsOffline(){
        ObservableList<Food> list = FXCollections.observableArrayList();
        list.add(food);

        when(requestInputOffline.getFoods()).thenReturn(list);
        assertEquals(list, model.getFoodsOffline());
    }

    @Test
    public void measureOffline(){
        Measure[] m = new Measure[1];
        m[0] = measure;

        when(requestInputOffline.measure(food)).thenReturn(m);
        assertEquals(m, model.measureOffline(food));
    }

    @Test
    public void searchIngredientOffline(){
        when(requestInputOffline.searchIngredient("food", "nurtrition", "3")).thenReturn(null);
        assertNull(model.searchIngredientOffline("food", "nurtrition", "3"));
    }

    @Test
    public void nextPageOffline(){
        ObservableList<Food> list = FXCollections.observableArrayList();
        list.add(food);

        when(requestInputOffline.nextPage()).thenReturn(false);
        assertFalse(model.nextPageOffline());
    }

    @Test
    public void getSelectedFood(){
        List<SelectedFood> added = new ArrayList<>();
        SelectedFood sf = new SelectedFood(food, measure, 3);
        added.add(sf);
        when(requestInputOnline.getFoodList()).thenReturn(added);
        when(requestInputOffline.getFoodList()).thenReturn(added);
        assertEquals(model.getSelectedFoodOnline(), added);
        assertEquals(model.getSelectedFoodOffline(), added);
    }

    @Test
    public void getMax(){
        Map<String, Integer> max = new HashMap<>();
        max.put("1", 1);
        max.put("2", 2);
        max.put("3", 3);

        when(requestInputOnline.getMax()).thenReturn(max);
        when(requestInputOffline.getMax()).thenReturn(max);
        assertEquals(model.getMaxOffline(), max);
        assertEquals(model.getMaxOnline(), max);
    }

    /*
    ------------------------------------------------------------------------------------------------
    FEATURE EXTENSION
    ------------------------------------------------------------------------------------------------
     */

    /**
     * Test the maximums() method in GameWindow
     */
    @Test
    public void maximumsTest(){
        Map<String, Integer> max = new HashMap<>();
        max.put("1", 1);
        max.put("2", 2);
        max.put("3", 3);

        Map<String, Integer> max1 = new HashMap<>();
        max1.put("1", 3);
        max1.put("2", 6);
        max1.put("3", 9);

        model.setMode("Meal prep");
        model.setModeNumber(3);
        assertEquals(max1, model.maximums(max));
    }

    /**
     * Test the nutrition() method in GameWindow
     */
    @Test
    public void nutritionTest(){
        List<String> l = new ArrayList<>();
        l.add("test1: 3 g");
        l.add("test2: 4 g");
        l.add("test3: 5 g");

        List<String> l1 = new ArrayList<>();
        l1.add("test1: 9.0 g");
        l1.add("test2: 12.0 g");
        l1.add("test3: 15.0 g");

        model.setMode("Meal prep");
        model.setModeNumber(3);
        assertEquals(l1, model.nutrition(l));
    }

    /**
     * Test the quantity() method in GameWindow
     */
    @Test
    public void quantityTest(){
        model.setMode("Meal prep");
        model.setModeNumber(3);
        assertEquals(12, model.quantity(4));
    }

    /**
     * Test the getModeName() method in GameWindow
     */
    @Test
    public void getModeNameTest(){
        model.setMode("Meal prep");
        assertEquals("Meal prep", model.getModeName());
    }

    /**
     * Test the getModeNumber() method in GameWindow
     */
    @Test
    public void getModeNumberTest(){
        model.setModeNumber(5);
        assertEquals(5, model.getModeNumber());
    }
}
