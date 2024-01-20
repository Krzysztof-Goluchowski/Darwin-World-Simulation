package org.model.presenter;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.model.*;
import org.model.util.ConsoleMapDisplay;
import javafx.stage.Screen;
import javafx.geometry.Rectangle2D;

import java.awt.event.HierarchyBoundsAdapter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SimulationPresenter {

    @FXML
    private TextField startingAmountOfPlantsLabel;
    @FXML
    private TextField minReproduceEnergyLabel;
    @FXML
    private TextField energyLostOnReproductionLabel;
    @FXML
    private TextField minMutationsLabel;
    @FXML
    private TextField maxMutationsLabel;
    @FXML
    private TextField newPlantPerDayLabel;
    @FXML
    private TextField energyLostPerDayLabel;
    @FXML
    private TextField genotypeSizeLabel;
    @FXML
    private TextField plantEnergyLabel;
    @FXML
    private TextField startingAnimalEnergyLabel;
    @FXML
    private TextField mapHeightLabel;
    @FXML
    private TextField mapWidthLabel;
    @FXML
    private TextField animalsAmountOnStartLabel;
    @FXML
    private TextField numberOfTunnelsLabel;
    @FXML
    private ComboBox<SimulationParameters.MutationVariant> mutationVariantComboBox;
    @FXML
    private ComboBox<SimulationParameters.MapVariant> mapVariantComboBox;
    @FXML
    private GridPane mapGrid;

    private Board worldMap;

    public void setWorldMap(Board worldMap) {
        this.worldMap = worldMap;
    }


    @FXML
    public void onSimulationStartClicked() throws IOException {
        SimulationParameters simulationParameters = getParameters();

        ArrayList<Animal> animalList = generateAnimals(simulationParameters);

        Board map = configureMap(simulationParameters);

        Simulation simulation = new Simulation(simulationParameters, map, animalList);
        SimulationEngine engine = new SimulationEngine(List.of(simulation));

        createSimulationStage(map);

        engine.runAsync();
    }

    private Board configureMap(SimulationParameters simulationParameters) {
        int mapHeight = Integer.parseInt(mapHeightLabel.getText());
        int mapWidth = Integer.parseInt(mapWidthLabel.getText());
        int numberOfTunnels = Integer.parseInt(numberOfTunnelsLabel.getText());

        return createMap(simulationParameters.getMapVariant(), mapWidth, mapHeight, numberOfTunnels);
    }

    private Board createMap(SimulationParameters.MapVariant mapVariant, int mapWidth, int mapHeight, int numberOfTunnels) {
        if (mapVariant == SimulationParameters.MapVariant.TUNNELS) {
            return new BoardWithTunnels(mapWidth, mapHeight, numberOfTunnels);
        } else {
            return new Board(mapWidth, mapHeight);
        }
    }

    private void createSimulationStage(Board map) throws IOException {
        Stage simulationStage = new Stage();
        simulationStage.setTitle("Running simulation");

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("simulation.fxml"));
        BorderPane viewRoot = loader.load();
        Scene scene = new Scene(viewRoot);

        SimulationPresenter presenter = loader.getController();
        presenter.setWorldMap(map);

        ConsoleMapDisplay observer = new ConsoleMapDisplay(presenter);
        map.setObserver(observer);

        setSimulationStageSize(simulationStage);

        simulationStage.setScene(scene);
        simulationStage.show();
        presenter.drawMap();
    }

    private void setSimulationStageSize(Stage simulationStage) {
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getBounds();
        double screenWidth = bounds.getWidth();
        double screenHeight = bounds.getHeight();
        simulationStage.setWidth(screenWidth);
        simulationStage.setHeight(screenHeight);
    }
    @FXML
    private SimulationParameters getParameters(){
        int startingAmountOfPlants = Integer.parseInt(startingAmountOfPlantsLabel.getText());
        int minReproduceEnergy = Integer.parseInt(minReproduceEnergyLabel.getText());
        int energyLostOnReproduction = Integer.parseInt(energyLostOnReproductionLabel.getText());
        int minMutations = Integer.parseInt(minMutationsLabel.getText());
        int maxMutations = Integer.parseInt(maxMutationsLabel.getText());
        int newPlantsPerDay = Integer.parseInt(newPlantPerDayLabel.getText());
        int energyLostPerDay = Integer.parseInt(energyLostPerDayLabel.getText());
        int genotypeSize = Integer.parseInt(genotypeSizeLabel.getText());
        int plantEnergy = Integer.parseInt(plantEnergyLabel.getText());
        int startingAnimalEnergy = Integer.parseInt(startingAnimalEnergyLabel.getText());


        String mutationVariantString = String.valueOf(mutationVariantComboBox.getValue());
        String mapVariantString = String.valueOf(mapVariantComboBox.getValue());

        SimulationParameters.MutationVariant mutationVariant;
        if (Objects.equals(mutationVariantString, "RANDOM")){
            mutationVariant = SimulationParameters.MutationVariant.RANDOM;
        } else {
            mutationVariant = SimulationParameters.MutationVariant.SWAP;
        }

        SimulationParameters.MapVariant mapVariant;
        if (Objects.equals(mapVariantString, "STANDARD")){
            mapVariant = SimulationParameters.MapVariant.STANDARD;
        } else {
            mapVariant = SimulationParameters.MapVariant.TUNNELS;
        }

        return new SimulationParametersBuilder()
                .startingAmountOfPlants(startingAmountOfPlants)
                .minReproduceEnergy(minReproduceEnergy)
                .energyLostOnReproduction(energyLostOnReproduction)
                .minMutations(minMutations)
                .maxMutations(maxMutations)
                .mutationVariant(mutationVariant)
                .mapVariant(mapVariant)
                .plantEnergy(plantEnergy)
                .newPlantPerDay(newPlantsPerDay)
                .energyLostPerDay(energyLostPerDay)
                .genotypeSize(genotypeSize)
                .startingAnimalEnergy(startingAnimalEnergy)
                .build();
    }

    public void drawMap() {
        clearGrid();

        for (int i = 0; i < worldMap.getWidth(); i++) {
            mapGrid.getColumnConstraints().add(new ColumnConstraints(30));
        }

        for (int j = 0; j < worldMap.getHeight(); j++) {
            mapGrid.getRowConstraints().add(new RowConstraints(30));
        }

        for (int i = 0 ; i < worldMap.getWidth(); i++) {
            for (int j = 0; j < worldMap.getHeight(); j++) {
                Label label = new Label();
                Vector2D position = new Vector2D(i, j);
                if (worldMap.objectAt(position) == null){
                    label.setText(" ");
                } else {
                    label.setText(worldMap.objectAt(position).toString());
                }

                GridPane.setHalignment(label, HPos.CENTER);
                mapGrid.add(label, i, j);
            }
        }
    }

    private void clearGrid() {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().get(0)); // hack to retain visible grid lines
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }

    private ArrayList<Animal> generateAnimals(SimulationParameters simulationParameters) {
        int animalsAmountOnStart = Integer.parseInt(animalsAmountOnStartLabel.getText());
        ArrayList<Animal> animalList = new ArrayList<>();
        for (int i = 0; i < animalsAmountOnStart; i++){
            animalList.add(new Animal(simulationParameters));
        }
        return animalList;
    }
}
