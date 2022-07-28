package project.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.text.Text;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * ModeController class extends Controller abstract class
 * This class will set the information to the GUI
 */
public class ModeController extends Controller{
    @FXML
    private ChoiceBox<String> mode;
    @FXML
    private ChoiceBox<Integer> number;
    @FXML
    private Text modeMsg, numberMsg, current;
    @FXML
    private AnchorPane mealPane;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        mode.getItems().addAll("Normal", "Meal prep");
        number.getItems().addAll(1,2,3,4,5,6,7);
    }

    /**
     * Change to the main page
     * @param event
     * @throws IOException
     */
    @FXML
    public void backAction(ActionEvent event) throws IOException {
        window.setFxml("main.fxml");
    }

    /**
     * Switch mode: "normal" and "meal prep"
     * @param event
     */
    @FXML
    public void changeButton(ActionEvent event){
        if(mode.getValue() == null){
            modeMsg.setText("Please select a mode");
            return;
        }
        modeMsg.setText("Your mode changes to "+mode.getValue());
        window.getPresenter().setMode(mode.getValue());
        if(window.getPresenter().getModeName().equals("Meal prep")){
            mealPane.setVisible(true);
        }else{
            mealPane.setVisible(false);
        }
    }

    /**
     * it will set the number of meals under "meal prep" mode
     * @param event
     */
    @FXML
    public void setButton(ActionEvent event){
        if(number.getValue() == null){
            numberMsg.setText("Please select a number");
            return;
        }
        numberMsg.setText("Your current number of meals is "+number.getValue());
        window.getPresenter().setModeNumber(number.getValue());
    }

    /**
     * Update the text about the current mode status
     */
    @Override
    public void modeUpdate(){
        if(window.getPresenter().getModeName().equals("Meal prep")){
            mealPane.setVisible(true);
            current.setText("Your current mode is 'Meal prep'");
        }else{
            mealPane.setVisible(false);
            current.setText("Your current mode is 'Normal'");
        }
    }
}
