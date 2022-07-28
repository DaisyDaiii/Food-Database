package project.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import project.posts.*;
import javafx.scene.chart.CategoryAxis;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * MainController class extends Controller abstract class
 * This class will set the information to the GUI
 */
public class MainController extends Controller {
    @FXML
    private AnchorPane searchPane, foodPane, pane, about, emailPane, addedPane, tipPane, progress;
    @FXML
    private Text error, pageError, searchError, emailError, mode;
    @FXML
    private TextField quantity, max, keyword, receiver;
    @FXML
    private ChoiceBox<Food> foodChoose;
    @FXML
    private ChoiceBox<String> nutrientsChoose;
    @FXML
    private ChoiceBox<Measure> measureChoose;
    @FXML
    private ListView<String> nutrientsShow, showList;
    @FXML
    private ListView<Food> foodsList;
    @FXML
    private Button clearCache, email, food;
    @FXML
    private ProgressIndicator indicator;

    private int session = -1;
    private boolean searchOnce = false;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        nutrientsChoose.getItems().addAll(FXCollections.observableArrayList("SUGAR.added", "CA", "CHOCDF.net", "CHOCDF",
                "CHOLE", "ENERC_KCAL", "FAMS", "FAPU", "FASAT", "FATRN", "FIBTG",
                "FOLDFE", "FOLFD", "FOLAC", "FE", "MG", "NIA", "P", "K", "PROCNT",
                "RIBF", "NA", "Sugar.alcohol", "SUGAR", "THIA", "FAT", "VITA_RAE",
                "VITB12", "VITB6A", "VITC", "VITD", "TOCPHA", "VITK1", "WATER", "ZN"));

        indicator.setProgress(-1);
    }

    /**
     * Change to the search page
     * @param event
     */
    @FXML
    public void searchPaneAction(ActionEvent event){
        searchPane.setVisible(true);
        foodPane.setVisible(false);
        about.setVisible(false);
        emailPane.setVisible(false);
        addedPane.setVisible(false);
        tipPane.setVisible(false);
        progress.setVisible(false);

        email.setDisable(false);
        food.setDisable(false);

        clear();
    }

    /**
     * Change to the food page
     * @param event
     */
    @FXML
    public void foodButton(ActionEvent event){
        searchPane.setVisible(false);
        about.setVisible(false);
        emailPane.setVisible(false);
        addedPane.setVisible(false);
        progress.setVisible(false);

        if(searchOnce){
            foodPane.setVisible(true);
            tipPane.setVisible(false);
        }else{
            tipPane.setVisible(true);
            foodPane.setVisible(false);
        }

        if(window.getPresenter().getOutputStatus().equals("offline")){
            clearCache.setVisible(false);
        }

        email.setDisable(false);
        food.setDisable(true);

        clear();
    }

    /**
     * ActionEvent representing some type of action.
     * After search button clicked, check the search key word, nutrition and max value
     * if it is valid, turn to Food page,
     * if not, ask input again
     * @param event
     */
    @FXML
    public void searchKeyAction(ActionEvent event){
        searchError.setText("");
        String f = keyword.getText();

        if(f.equals("")){
            searchError.setText("Must enter an ingredient");
            return;
        }

        String nur = (String)nutrientsChoose.getValue();

        String num = max.getText();
        if(!num.isEmpty() && !num.matches("\\d+")){
            searchError.setText("Please enter integers for max value");
            return;
        }

        if(window.getPresenter().getOutputStatus().equals("offline")){
            clearCache.setVisible(false);
        }

        progress.setVisible(true);
        window.getPresenter().SearchConcurrency(f, nur, num);

        searchError.setText("Success!");
        foodPane.setVisible(true);
        tipPane.setVisible(false);
        food.setDisable(true);
        searchOnce = true;
    }

    /**
     * Clear the text in the GUI
     */
    private void clear(){
        keyword.clear();
        max.clear();

        window.getPresenter().clearCurrentNur();

        error.setText("");
        searchError.setText("");
        pageError.setText("");
        emailError.setText("");
    }

    /**
     * ActionEvent representing some type of action.
     * After choice box clicked, it will be filled with mesures
     * @param event
     */
    @FXML
    public void measureAction(MouseEvent event){
        if(measureChoose.getItems().size()>0){
            measureChoose.getItems().clear();
        }
        Food f = (Food) foodChoose.getValue();
        if(f == null){
            error.setText("Please select a food");
            return;
        }

        Measure[] m = window.getPresenter().getMeasure(f);
        measureChoose.getItems().addAll(Arrays.asList(m));
    }

    /**
     * ActionEvent representing some type of action.
     * After previous button clicked, check whether has the previous page
     * if it exists, change to previous paoge
     * if not, show the error
     * @param event
     */
    @FXML
    public void previousAction(ActionEvent event){
        pageError.setText("");
        if(session == -1){
            pageError.setText("Please search ingredients first");
            return;
        }
        if(session == 0){
            pageError.setText("Already first page");
            return;
        }

        session-=40; // control the page

        progress.setVisible(true);
        window.getPresenter().OnlinePrevPageConcurrency(session);
    }

    /**
     * ActionEvent representing some type of action.
     * After next button clicked, check whether has the next page
     * if it exists, change to the next page
     * if not, show the error
     * @param event
     */
    @FXML
    public void nextAction(ActionEvent event){
        pageError.setText("");
        if(session == -1){
            pageError.setText("Please search ingredients first");
            return;
        }

        if(window.getPresenter().getOutputStatus().equals("offline")){
            pageError.setText("Already last page");
            return;
        }

        progress.setVisible(true);
        window.getPresenter().OnlineNextPageConcurrency();
    }

    /**
     * ActionEvent representing some type of action.
     * After add button clicked, check the food, quantity, measure
     * if it is valid, add the food and show the nutrition information
     * if not, show the error
     * @param event
     */
    @FXML
    public void addAction(ActionEvent event){
        Food f = (Food) foodChoose.getValue();
        if(f == null){
            error.setText("Please select a food");
            return;
        }

        String num = quantity.getText();
        if(!num.matches("^(-|\\+)?\\d+(\\.\\d+)?$")){
            error.setText("Please enter numbers");
            return;
        }

        Measure m = (Measure) measureChoose.getValue();
        if(m == null){
            error.setText("Please select a measure");
            return;
        }

        error.setText(" ");

        if(window.getPresenter().getOutputStatus().equals("offline")){
            progress.setVisible(true);
            window.getPresenter().OfflineAddIngredientConcurrency(f, m, num);
        }else{
            boolean refresh = true;

            if(window.getPresenter().getNurID(f, num, m) > -1){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Cache Hit for This Data");
                alert.setHeaderText("Do you want to use cache? (otherwise request fresh data from the API)");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK){
                    refresh = false;
                }
            }

            progress.setVisible(true);
            window.getPresenter().OnlineAddIngredientConcurrency(f, m, num, refresh);
        }
        quantity.clear();
        measureChoose.getItems().clear();
    }

    /**
     * Change to the added foods page
     * @param event
     */
    @FXML
    public void showButtonAction(ActionEvent event){
        showList.setItems(window.getPresenter().showAdded());

        addedPane.setVisible(true);
        searchPane.setVisible(false);
        foodPane.setVisible(false);
        about.setVisible(false);
        emailPane.setVisible(false);
        tipPane.setVisible(false);
        progress.setVisible(false);

        email.setDisable(false);
        food.setDisable(false);
    }

    /**
     * ActionEvent representing some type of action.
     * After max button clicked, it will show the stacked bar chart comparing each nutritional value against its set maximum
     * @param event
     */
    @FXML
    public void maxButtonAction(ActionEvent event){
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle(" Stacked bar ");
        dialog.setHeaderText("This is a stacked bar chart comparing each nutritional value against its set maximum");

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Nutrition");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Value");

        StackedBarChart<String, Number> stackedBar = new StackedBarChart<>(xAxis, yAxis);
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
        series1.setName("max");
        series2.setName("Nutritional value for current food");

        window.getPresenter().bar(series1, series2);

        stackedBar.getData().add(series1);
        stackedBar.getData().add(series2);

        dialog.getDialogPane().resize(400, 100);
        dialog.getDialogPane().setContent(stackedBar);
        dialog.showAndWait();
    }

    /**
     * Change to the send email page
     * @param event
     */
    @FXML
    public void emailButton(ActionEvent event){
        searchPane.setVisible(false);
        foodPane.setVisible(false);
        about.setVisible(false);
        emailPane.setVisible(true);
        addedPane.setVisible(false);
        tipPane.setVisible(false);
        progress.setVisible(false);

        email.setDisable(true);
        food.setDisable(false);

        clear();
    }

    /**
     * ActionEvent representing some type of action.
     * After add send email clicked, check the reveiver email
     * if it is valid, send the email
     * if not, show the error
     * @param event
     */
    @FXML
    public void sendButton(ActionEvent event){
        String to = receiver.getText();
        if(to.isEmpty()){
            emailError.setText("Receiver cannot be null");
            return;
        }

        progress.setVisible(true);
        window.getPresenter().EmailConcurrency(to);

        receiver.clear();
    }

    /**
     * Change to the about page
     * @param event
     */
    @FXML
    public void aboutButton(ActionEvent event){
        searchPane.setVisible(false);
        foodPane.setVisible(false);
        about.setVisible(true);
        emailPane.setVisible(false);
        addedPane.setVisible(false);
        tipPane.setVisible(false);
        progress.setVisible(false);

        email.setDisable(false);
        food.setDisable(false);

        clear();
    }

    /**
     * ActionEvent representing some type of action.
     * After snapshot button clicked, choose a file and save the snapshot
     * @param event
     */
    @FXML
    public void snapshotButton(ActionEvent event){
        WritableImage image = pane.snapshot(new SnapshotParameters(), null);

        FileChooser fc = new FileChooser();
        Stage s = new Stage();

        fc.setTitle("Save Image");
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        File file = fc.showOpenDialog(s);

        if (file != null) {
            String name = file.getName();
            String extension = name.substring(1+name.lastIndexOf(".")).toLowerCase();
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), extension, file);
            } catch (IOException e) {
                System.out.println("fail");
            }
        }
    }

    /**
     * ActionEvent representing some type of action.
     * After clear button clicked, clear the database
     * @param event
     */
    @FXML
    public void clearButton(ActionEvent event){
        window.getPresenter().newDB();
        error.setText("clear cache successfully");
    }

    /*
    ------------------------------------------------------------------------------------------------
    CONCURRENCY FUNCTIONS
    ------------------------------------------------------------------------------------------------
     */

    /**
     * refresh the page when the search button clicked
     * @param get the value get from model
     */
    public void refreshSearch(String get){
        if(get != null){
            searchError.setText(get);
        }

        foodChoose.getItems().clear();
        foodChoose.getItems().addAll(window.getPresenter().getFoods());

        session = 0;
        foodsList.setItems(window.getPresenter().getFoods());
        clear();

        progress.setVisible(false);
    }

    /**
     * refresh the page when the next button clicked
     * @param list the value get from model
     */
    public void refreshNext(ObservableList<Food> list){
        if(list == null){
            pageError.setText("Already last page");
        }else{
            foodChoose.getItems().clear();
            foodChoose.getItems().addAll(list);
            foodsList.setItems(list);
            session+=40;
        }

        progress.setVisible(false);
    }

    /**
     * refresh the page when the previous button clicked
     * @param list the value get from model
     */
    public void refreshPrev(ObservableList<Food> list){
        foodChoose.getItems().clear();
        foodChoose.getItems().addAll(list);
        foodsList.setItems(list);

        progress.setVisible(false);
    }

    /**
     * refresh the page when the add ingredient button clicked online
     * @param list the value get from model
     */
    public void refreshOnlineIngredient(ObservableList<String> list){
        nutrientsShow.setItems(list);
        progress.setVisible(false);
    }

    /**
     * refresh the page when the add ingredient button clicked offline
     * @param list the value get from model
     */
    public void refreshOfflineIngredient(ObservableList<String> list){
        nutrientsShow.setItems(list);
        progress.setVisible(false);
    }

    /**
     * refresh the page when send email button clicked
     * @param get the value get from model
     */
    public void refreshEmail(String get){
        emailError.setText(get);
        progress.setVisible(false);
    }

    /*
    ------------------------------------------------------------------------------------------------
    FEATURE EXTENSION
    ------------------------------------------------------------------------------------------------
     */

    /**
     * Change to the mode page
     * @param event
     * @throws IOException
     */
    @FXML
    public void modeAction(ActionEvent event) throws IOException{
        window.setFxml("mode.fxml");
    }

    /**
     * Show the current mode status
     */
    @Override
    public void modeUpdate(){
        String m = "Current mode: "+window.getPresenter().getModeName();
        if(window.getPresenter().getModeName().equals("Meal prep")){
            m+="      The number of meals is "+window.getPresenter().getModeNumber();
        }
        mode.setText(m);
    }
}
