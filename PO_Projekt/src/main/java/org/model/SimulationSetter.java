package org.model;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.model.presenter.SimulationPresenter;

import java.io.IOException;

public class SimulationSetter extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("Menu.fxml"));
        GridPane viewRoot = loader.load();
        viewRoot.setMinWidth(900);
        viewRoot.setMinHeight(800);

        Scene scene = new Scene(viewRoot);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Simulation app");
        primaryStage.show();
    }
}