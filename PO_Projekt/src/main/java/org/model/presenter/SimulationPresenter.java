package org.model.presenter;

import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.model.*;
import org.model.util.ConsoleMapDisplay;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SimulationPresenter {
    private Board worldMap;
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

    public void setWorldMap(Board worldMap) {
        this.worldMap = worldMap;
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

//        return new SimulationParameters(startingAmountOfPlants, minReproduceEnergy, energyLostOnReproduction, minMutations, maxMutations, mutationVariant, mapVariant, plantEnergy, newPlantsPerDay, energyLostPerDay, genotypeSize, startingAnimalEnergy);
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
    @FXML
    public void onSimulationStartClicked(){
        SimulationParameters simulationParameters = getParameters();

        int mapHeight = Integer.parseInt(mapHeightLabel.getText());
        int mapWidth = Integer.parseInt(mapWidthLabel.getText());
        int numberOfTunnels = Integer.parseInt(numberOfTunnelsLabel.getText());

        ArrayList<Animal> animalList = generateAnimals(simulationParameters);

        Board map;
        if (simulationParameters.getMapVariant() == SimulationParameters.MapVariant.TUNNELS){
            map = new BoardWithTunnels(mapWidth, mapHeight, numberOfTunnels);
        } else {
            map = new Board(mapWidth, mapHeight);
        }

        ConsoleMapDisplay observer = new ConsoleMapDisplay();
        map.setObserver(observer);

        Simulation simulation = new Simulation(simulationParameters, map, animalList);
        SimulationEngine engine = new SimulationEngine(List.of(simulation));

        // Tworzymy nowe okno dla symulacji
        Stage simulationStage = new Stage();
        simulationStage.setTitle("Wyniki symulacji");

        // Tworzymy VBox (lub inny rodzaj kontenera) dla zawartości nowego okna
        VBox vbox = new VBox();
        vbox.getChildren().add(new Label("Wyniki symulacji będą tutaj."));

        Scene simulationScene = new Scene(vbox, 300, 200);
        simulationStage.setScene(simulationScene);

        // Ustawiamy, że nowe okno jest modalne (blokuje interakcję z głównym oknem, dopóki nie zostanie zamknięte)
        simulationStage.initModality(Modality.NONE);

        // Pokazujemy nowe okno
        simulationStage.show();

        engine.runAsync();
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
