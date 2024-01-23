package org.model;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.control.ScrollPane;
import java.io.IOException;
import java.util.Objects;

public class SimulationSetter extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("Menu.fxml"));
        GridPane viewRoot = loader.load();

        ScrollPane scrollPane = new ScrollPane(viewRoot);
        scrollPane.getStyleClass().add("scroll-pane");

        Scene scene = new Scene(scrollPane, 565, 1000);
        scene.getStylesheets().add(getClass().getClassLoader().getResource("css/styles.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setTitle("Simulation app");
        primaryStage.show();
    }
}