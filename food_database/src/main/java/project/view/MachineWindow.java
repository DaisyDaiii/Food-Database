package project.view;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import project.presenter.Presenter;

import java.io.IOException;

public class MachineWindow {
    Scene scene;
    Stage stage;
    Presenter presenter;


    public MachineWindow(Stage primaryStage, Presenter presenter) throws IOException {
        this.stage = primaryStage;
        this.presenter = presenter;
        setFxml("main.fxml");
    }

    public void setFxml(String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource(fxml));
        Pane pane = (Pane)loader.load();
        try {
            ((Controller) loader.getController()).setApp(this);
            ((Controller) loader.getController()).modeUpdate();
        }catch(NullPointerException e){}
        setScene(new Scene(pane,1280,720));
        if(fxml.equals("main.fxml")){
            presenter.setView((MainController) loader.getController());
        }
    }

    public void setScene(Scene scene){ this.scene = scene; stage.setScene(scene);}

    public Scene getScene() {
        return this.scene;
    }

    public Presenter getPresenter() {
        return presenter;
    }
}
