package org.model;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.ArrayList;

public class SimulationSetter extends Application {
    // Atrybuty klasy dla elementów interfejsu użytkownika
    private final TextField startingAmountOfPlantsField = new TextField();
    private final TextField minReproduceEnergyField = new TextField();
    private final TextField energyLostOnReproductionField = new TextField();
    private final TextField minMutationsField = new TextField();
    private final TextField maxMutationsField = new TextField();
    private final TextField newPlantPerDayField = new TextField();
    private final TextField energyLostPerDayField = new TextField();
    private final TextField genotypeSizeField = new TextField();
    private final TextField plantEnergyField = new TextField();
    private final TextField startingAnimalEnergyField = new TextField();
    private final TextField mapHeightField = new TextField();
    private final TextField mapWidthField = new TextField();
    private final TextField animalsAmountOnStartField = new TextField();
    private final TextField numberOfTunnelsField = new TextField();
    private final ComboBox<SimulationParameters.MutationVariant> mutationVariantComboBox = new ComboBox<>();
    private final ComboBox<SimulationParameters.MapVariant> mapVariantComboBox = new ComboBox<>();

    @Override
    public void start(Stage primaryStage) {
        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);

        // Ustawianie etykiet i pól tekstowych
        Label minReproduceEnergyLabel = new Label("Min Reproduce Energy:");
        Label energyLostOnReproductionLabel = new Label("Energy Lost on Reproduction:");
        Label minMutationsLabel = new Label("Min Mutations:");
        Label maxMutationsLabel = new Label("Max Mutations:");
        Label newPlantPerDayLabel = new Label("New Plant Per Day:");
        Label energyLostPerDayLabel = new Label("Energy Lost Per Day:");
        Label genotypeSizeLabel = new Label("Genotype Size:");
        Label plantEnergyLabel = new Label("Plant Energy:");
        Label startingAnimalEnergyLabel = new Label("Starting Animal Energy:");
        Label startingAmountOfPlantsLabel = new Label("Starting amount of plants: ");
        Label mapVariantLabel = new Label("Map variant: ");
        Label mutationVariantLabel = new Label("Mutation variant: ");
        Label mapHeightLabel = new Label("Map height: ");
        Label mapWidthLabel = new Label("Map width: ");
        Label animalsAmountOnStartLabel = new Label("How many animals on start: ");
        Label numberOfTunnelsLabel = new Label("Number of tunnels:");

        numberOfTunnelsLabel.setVisible(false);
        numberOfTunnelsField.setVisible(false);
        mapVariantComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == SimulationParameters.MapVariant.TUNNELS) {
                numberOfTunnelsLabel.setVisible(true);
                numberOfTunnelsField.setVisible(true);
            } else {
                numberOfTunnelsLabel.setVisible(false);
                numberOfTunnelsField.setVisible(false);
            }
        });

        mutationVariantComboBox.getItems().addAll(SimulationParameters.MutationVariant.values());

        mapVariantComboBox.getItems().addAll(SimulationParameters.MapVariant.values());

        setToDefault(); //Ustaw wartosci domyslne

        // Dodaj elementy do gridu
        grid.add(minReproduceEnergyLabel, 0, 1);
        grid.add(minReproduceEnergyField, 1, 1);

        grid.add(energyLostOnReproductionLabel, 0, 2);
        grid.add(energyLostOnReproductionField, 1, 2);

        grid.add(minMutationsLabel, 0, 3);
        grid.add(minMutationsField, 1, 3);

        grid.add(maxMutationsLabel, 0, 4);
        grid.add(maxMutationsField, 1, 4);

        grid.add(newPlantPerDayLabel, 0, 5);
        grid.add(newPlantPerDayField, 1, 5);

        grid.add(energyLostPerDayLabel, 0, 6);
        grid.add(energyLostPerDayField, 1, 6);

        grid.add(genotypeSizeLabel, 0, 7);
        grid.add(genotypeSizeField, 1, 7);

        grid.add(plantEnergyLabel, 0, 8);
        grid.add(plantEnergyField, 1, 8);

        grid.add(startingAnimalEnergyLabel, 0, 9);
        grid.add(startingAnimalEnergyField, 1, 9);

        grid.add(startingAmountOfPlantsLabel, 0, 10);
        grid.add(startingAmountOfPlantsField, 1, 10);

        grid.add(mapVariantLabel, 0, 11);
        grid.add(mapVariantComboBox, 1, 11);
        grid.add(numberOfTunnelsLabel, 2, 11);
        grid.add(numberOfTunnelsField, 3, 11);

        grid.add(mutationVariantLabel, 0, 12);
        grid.add(mutationVariantComboBox, 1, 12);

        grid.add(mapWidthLabel, 0, 13);
        grid.add(mapWidthField, 1, 13);

        grid.add(mapHeightLabel, 0, 14);
        grid.add(mapHeightField, 1, 14);

        grid.add(animalsAmountOnStartLabel, 0, 15);
        grid.add(animalsAmountOnStartField, 1, 15);

        Button runButton = new Button("Run Simulation");
        grid.add(runButton, 1, 16);
        runButton.setOnAction(e -> runSimulation());

        Scene scene = new Scene(grid, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Simulation Parameters");
        primaryStage.show();
    }

    private void runSimulation() {
        try {
            int startingAmountOfPlants = Integer.parseInt(startingAmountOfPlantsField.getText());
            int minReproduceEnergy = Integer.parseInt(minReproduceEnergyField.getText());
            int energyLostOnReproduction = Integer.parseInt(energyLostOnReproductionField.getText());
            int minMutations = Integer.parseInt(minMutationsField.getText());
            int maxMutations = Integer.parseInt(maxMutationsField.getText());
            int newPlantsPerDay = Integer.parseInt(newPlantPerDayField.getText());
            int energyLostPerDay = Integer.parseInt(energyLostPerDayField.getText());
            int genotypeSize = Integer.parseInt(genotypeSizeField.getText());
            int plantEnergy = Integer.parseInt(plantEnergyField.getText());
            int startingAnimalEnergy = Integer.parseInt(startingAnimalEnergyField.getText());
            int animalsAmountOnStart = Integer.parseInt(animalsAmountOnStartField.getText());
            int mapHeight = Integer.parseInt(mapHeightField.getText());
            int mapWidth = Integer.parseInt(mapWidthField.getText());
            int numberofTunnels = Integer.parseInt(numberOfTunnelsField.getText());

            SimulationParameters.MutationVariant mutationVariant = mutationVariantComboBox.getValue();
            SimulationParameters.MapVariant mapVariant = mapVariantComboBox.getValue();

            SimulationParameters parameters = new SimulationParameters(startingAmountOfPlants, minReproduceEnergy, energyLostOnReproduction, minMutations, maxMutations, mutationVariant, mapVariant, plantEnergy, newPlantsPerDay, energyLostPerDay, genotypeSize, startingAnimalEnergy);

            ArrayList<Animal> animalsList = new ArrayList<>();
            for (int i = 0; i < animalsAmountOnStart; i++) {
                Animal animal = new Animal(parameters);
                animalsList.add(animal);
            }

            //To chyba nie jest cleancodexD
            if (mapVariant == SimulationParameters.MapVariant.TUNNELS){
                BoardWithTunnels map = new BoardWithTunnels(mapWidth, mapHeight, numberofTunnels);
                Simulation simulation = new Simulation(parameters, map, animalsList);
                simulation.run();
            }
            else {
                Board map = new Board(mapWidth, mapHeight);
                Simulation simulation = new Simulation(parameters, map, animalsList);
                simulation.run();
            }

        } catch (NumberFormatException e) { //Zly format danych
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Wrong input format!");
            alert.setHeaderText("Dumny ty jestes z siebie?");
            alert.setContentText("Enter valid input format");
            alert.showAndWait();

            setToDefault();
        }
    }

    private void setToDefault() {
        minReproduceEnergyField.setText("10");
        energyLostPerDayField.setText("5");
        minMutationsField.setText("1");
        maxMutationsField.setText("3");
        newPlantPerDayField.setText("5");
        genotypeSizeField.setText("5");
        plantEnergyField.setText("3");
        startingAmountOfPlantsField.setText("10");
        energyLostOnReproductionField.setText("3");
        startingAnimalEnergyField.setText("15");
        mapWidthField.setText("5");
        mapHeightField.setText("5");
        animalsAmountOnStartField.setText("2");
        numberOfTunnelsField.setText("5");

        mutationVariantComboBox.setValue(SimulationParameters.MutationVariant.RANDOM);
        mapVariantComboBox.setValue(SimulationParameters.MapVariant.STANDARD);
    }
}