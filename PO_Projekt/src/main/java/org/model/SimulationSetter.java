package org.model;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.control.ScrollPane;
import java.awt.*;
import java.io.IOException;

public class SimulationSetter extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("Menu.fxml"));
        GridPane viewRoot = loader.load();

        ScrollPane scrollPane = new ScrollPane(viewRoot);



        Scene scene = new Scene(scrollPane, 400, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Simulation app");
        primaryStage.show();
    }
}