package project;

import javafx.application.Application;
import javafx.stage.Stage;
import project.model.*;
import project.presenter.Presenter;
import project.view.MachineWindow;

import java.io.IOException;

public class App extends Application{
    private static String inputStatus = null;
    private static String outputStatus = null;

    public static void main(String[] args) {
        inputStatus = args[0];
        outputStatus = args[1];

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Food Database");
        primaryStage.setWidth(1280);
        primaryStage.setHeight(720);
        primaryStage.setResizable(false);

        RequestInputOnline requestInputOnline = new RequestInputOnline();
        RequestInputOffline requestInputOffline = new RequestInputOffline();
        RequestOutputOnline requestOutputOnline = new RequestOutputOnline(requestInputOnline);
        RequestOutputOffline requestOutputOffline = new RequestOutputOffline();

        GameModel model = new GameModel(requestInputOnline, requestInputOffline, requestOutputOnline, requestOutputOffline);
        Presenter presenter = new Presenter(model, inputStatus, outputStatus);
        MachineWindow window = new MachineWindow(primaryStage, presenter);
        primaryStage.setScene(window.getScene());
        primaryStage.show();
    }

}
