package org.model.presenter;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.model.*;
import org.model.util.ConsoleMapDisplay;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.*;


public class SimulationPresenter implements Initializable, SimulationObserver {

    @FXML
    private Spinner<Integer> startingAmountOfPlantsLabel, minReproduceEnergyLabel, energyLostOnReproductionLabel,
            minMutationsLabel, maxMutationsLabel, newPlantPerDayLabel, energyLostPerDayLabel,
            genotypeSizeLabel, plantEnergyLabel, startingAnimalEnergyLabel, mapHeightLabel,
            mapWidthLabel, animalsAmountOnStartLabel, numberOfTunnelsLabel;

    @FXML
    private ComboBox<String> mutationVariantComboBox, mapVariantComboBox;

    @FXML
    private GridPane mapGrid;

    @FXML
    private Label animalCountLabel, plantCountLabel, averageEnergyLabel, averageLifespanLabel, freeSpotsLabel,
            mostPopularGenotypeLabel, averageNumberOfChildrenLabel, simulationDayLabel, animalGenotype,
            animalActiveGenome, animalEnergy, animalCountOfConsumedPlants, animalCountOfChildren,
            animalCountOfDescendant, animalDaysSurvived, animalDayOfDeath;

    @FXML
    private ListView<String> defaultConfigurationsListView;

    @FXML
    private CheckBox createCSVCheckBox;

    private SimulationEngine engine;
    private Board worldMap;
    private final String[] defaultSettings = {"Urodzaj", "Posucha", "Niekonczaca sie opowiesc"};
    private final HashMap<String, int[]> settings = new HashMap<>();
    private SimulationPresenter presenter;
    private PrintWriter csvWriter;
    private boolean saveToCSV;
    private boolean isPaused;
    private int referenceEnergy;
    private ArrayList<Animal> animalArrayList;
    private Animal trackedAnimal;
    private SimulationParameters simulationParameters;

    public void setWorldMap(Board worldMap) {
        this.worldMap = worldMap;
    }
    public void setSimulationParameters(SimulationParameters simulationParameters){
        this.simulationParameters = simulationParameters;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (defaultConfigurationsListView != null) {

            defaultConfigurationsListView.getItems().addAll(defaultSettings);

            settings.put("Urodzaj", new int[]{5, 5, 0, 2, 15, 5, 3, 1, 5, 15, 3, 0, 0, 25, 40, 0});
            settings.put("Posucha", new int[]{10, 10, 2, 5, 5, 1, 2, 2, 10, 10, 5, 0, 0, 30, 30, 0});
            settings.put("Niekonczaca sie opowiesc", new int[]{5, 5, 2, 3, 20, 10, 4, 0, 7, 15, 4, 0, 0, 30, 50, 0});
            defaultConfigurationsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                String selectedOption = defaultConfigurationsListView.getSelectionModel().getSelectedItem();

                int[] parameters = settings.get(selectedOption);

                minReproduceEnergyLabel.getValueFactory().setValue(parameters[0]);
                energyLostOnReproductionLabel.getValueFactory().setValue(parameters[1]);
                minMutationsLabel.getValueFactory().setValue(parameters[2]);
                maxMutationsLabel.getValueFactory().setValue(parameters[3]);
                startingAmountOfPlantsLabel.getValueFactory().setValue(parameters[4]);
                newPlantPerDayLabel.getValueFactory().setValue(parameters[5]);
                plantEnergyLabel.getValueFactory().setValue(parameters[6]);
                energyLostPerDayLabel.getValueFactory().setValue(parameters[7]);
                genotypeSizeLabel.getValueFactory().setValue(parameters[8]);
                startingAnimalEnergyLabel.getValueFactory().setValue(parameters[9]);
                animalsAmountOnStartLabel.getValueFactory().setValue(parameters[10]);

                if (parameters[11] == 0){
                    mapVariantComboBox.setValue("STANDARD");
                } else {
                    mapVariantComboBox.setValue("TUNNELS");
                }

                if (parameters[12] == 0){
                    mutationVariantComboBox.setValue("RANDOM");
                } else {
                    mutationVariantComboBox.setValue("SWAP");
                }

                mapHeightLabel.getValueFactory().setValue(parameters[13]);
                mapWidthLabel.getValueFactory().setValue(parameters[14]);
                numberOfTunnelsLabel.getValueFactory().setValue(parameters[15]);
            });
        }
    }
    @FXML
    public void onSaveConfiguration(){
        String mapVariant = mapVariantComboBox.getValue();
        String mutationVariant = mutationVariantComboBox.getValue();

        int map, mutation;

        if (Objects.equals(mapVariant, "TUNNELS")){
            map = 1;
        } else {
            map = 0;
        }

        if (Objects.equals(mutationVariant, "SWAP")){
            mutation = 1;
        } else {
            mutation = 0;
        }

        int[] savedSettings = new int[]{
                minReproduceEnergyLabel.getValue(),
                energyLostOnReproductionLabel.getValue(),
                minMutationsLabel.getValue(),
                maxMutationsLabel.getValue(),
                startingAmountOfPlantsLabel.getValue(),
                newPlantPerDayLabel.getValue(),
                plantEnergyLabel.getValue(),
                energyLostPerDayLabel.getValue(),
                genotypeSizeLabel.getValue(),
                startingAnimalEnergyLabel.getValue(),
                animalsAmountOnStartLabel.getValue(),
                map,
                mutation,
                mapHeightLabel.getValue(),
                mapWidthLabel.getValue(),
                numberOfTunnelsLabel.getValue()

        };

        defaultConfigurationsListView.getItems().add("New Settings " + (settings.size() - 2));
        settings.put("New Settings " + (settings.size() - 2), savedSettings);
    }
    @FXML
    public void onDeleteConfiguration(){
        String selectedConfig = defaultConfigurationsListView.getSelectionModel().getSelectedItem();
        if (selectedConfig != null) {
            defaultConfigurationsListView.getItems().remove(selectedConfig);
            settings.remove(selectedConfig);
        }
    }
    @FXML
    public void onPauseClicked() {
        engine.pauseAllSimulations();
        this.isPaused = true;
        drawMap();
    }
    @FXML
    public void onResumeClicked() {
        this.isPaused = false;
        engine.resumeAllSimulations();
    }
    @FXML
    public void onSimulationStartClicked() throws IOException {
        SimulationParameters simulationParameters = getParameters();

        ArrayList<Animal> animalList = generateAnimals(simulationParameters);
        Board map = configureMap(simulationParameters);

        Simulation simulation = new Simulation(simulationParameters, map, animalList);

        SimulationEngine engine = new SimulationEngine(List.of(simulation));

        SimulationPresenter newPresenter = createSimulationStage(map, engine, animalList);

        simulation.addObserver(newPresenter);

        engine.runAsync();
    }

    private Board configureMap(SimulationParameters simulationParameters) {
        int mapHeight = mapHeightLabel.getValue();
        int mapWidth = mapWidthLabel.getValue();
        int numberOfTunnels = numberOfTunnelsLabel.getValue();

        return createMap(simulationParameters.getMapVariant(), mapWidth, mapHeight, numberOfTunnels);
    }

    private Board createMap(SimulationParameters.MapVariant mapVariant, int mapWidth, int mapHeight, int numberOfTunnels) {
        if (mapVariant == SimulationParameters.MapVariant.TUNNELS) {
            return new BoardWithTunnels(mapWidth, mapHeight, numberOfTunnels);
        } else {
            return new Board(mapWidth, mapHeight);
        }
    }

    public void setAnimalArrayList(ArrayList<Animal> animalArrayList) {
        this.animalArrayList = animalArrayList;
    }
    private SimulationPresenter createSimulationStage(Board map, SimulationEngine engine, ArrayList<Animal> animalArrayList) throws IOException {
        Stage simulationStage = new Stage();
        simulationStage.setTitle("Running simulation");

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("simulation.fxml"));
        BorderPane viewRoot = loader.load();
        Scene scene = new Scene(viewRoot);

        SimulationPresenter presenter = loader.getController();
        presenter.setWorldMap(map);
        presenter.referenceEnergy = startingAmountOfPlantsLabel.getValue();
        presenter.saveToCSV = createCSVCheckBox.isSelected();

        if(presenter.saveToCSV){
            try {
                FileWriter fileWriter = new FileWriter("simulation_stats.csv", true);
                presenter.csvWriter = new PrintWriter(fileWriter);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        presenter.setSimulationParameters(simulationParameters);
        presenter.setEngine(engine);
        presenter.setAnimalArrayList(animalArrayList);

        ConsoleMapDisplay observer = new ConsoleMapDisplay(presenter);
        map.setObserver(observer);

        setSimulationStageSize(simulationStage);

        simulationStage.setScene(scene);
        simulationStage.show();
        presenter.drawMap();

        return presenter;
    }

    public void setEngine(SimulationEngine engine) {
        this.engine = engine;
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

        int startingAmountOfPlants = startingAmountOfPlantsLabel.getValue();
        int minReproduceEnergy = minReproduceEnergyLabel.getValue();
        int energyLostOnReproduction = energyLostOnReproductionLabel.getValue();
        int minMutations = minMutationsLabel.getValue();
        int maxMutations = maxMutationsLabel.getValue();
        int newPlantsPerDay = newPlantPerDayLabel.getValue();
        int energyLostPerDay = energyLostPerDayLabel.getValue();
        int genotypeSize = genotypeSizeLabel.getValue();
        int plantEnergy = plantEnergyLabel.getValue();
        int startingAnimalEnergy = startingAnimalEnergyLabel.getValue();

        String mutationVariantString = mutationVariantComboBox.getValue();
        String mapVariantString = mapVariantComboBox.getValue();

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

    public synchronized void drawMap() {
        clearGrid();

        for (int i = 0; i < worldMap.getWidth(); i++) {
            mapGrid.getColumnConstraints().add(new ColumnConstraints(30));
        }

        for (int j = 0; j < worldMap.getHeight(); j++) {
            mapGrid.getRowConstraints().add(new RowConstraints(30));
        }

        if (trackedAnimal != null){
            updateTrackingAnimal(trackedAnimal);
        }

        for (int i = 0; i < worldMap.getWidth(); i++) {
            for (int j = 0; j < worldMap.getHeight(); j++) {
                StackPane cell = new StackPane();

                if (this.isPaused){
                    int middleOfBoard = worldMap.getHeight() / 2;
                    int radiusOfEquator = (int) (0.1 * worldMap.getHeight());
                    int bottomLimitOfEquator = middleOfBoard - radiusOfEquator;
                    int upperLimitOfEquator = middleOfBoard + radiusOfEquator;

                    // Wyróżnianie obszaru równikowego
                    if (j >= bottomLimitOfEquator && j <= upperLimitOfEquator) {
                        cell.setStyle("-fx-background-color: lightblue;");
                    }
                }

                Object mapObject = worldMap.objectAt(new Vector2D(i, j));

                if (mapObject instanceof Animal animal) {
                    Rectangle animalShape = new Rectangle(30, 30);
                    animalShape.setFill(Color.web(mapEnergyToColor(animal.getEnergy(), this.referenceEnergy)));
                    cell.getChildren().add(animalShape);

                    if (animal.hasMostPopularGenotype()) { // wyrozniam te z najpopularniejszym genotypem
                        Circle highlightCircle = new Circle(10);
                        highlightCircle.setFill(Color.PURPLE);
                        cell.getChildren().add(highlightCircle);
                    }

                    if (animal.equals(trackedAnimal)) {
                        Polygon triangle = new Polygon();
                        triangle.getPoints().addAll(
                                15.0, 0.0,
                                0.0, 30.0,
                                30.0, 30.0
                        );
                        triangle.setFill(Color.BLUE);
                        cell.getChildren().add(triangle);
                    }

                } else if (mapObject instanceof Plant) {
                    Circle plantShape = new Circle(15);
                    plantShape.setFill(Color.GREEN);
                    cell.getChildren().add(plantShape);
                }

                if (worldMap instanceof BoardWithTunnels){
                    Map<Vector2D, Vector2D> tunnels = worldMap.getTunnelsMaps();
                    Vector2D tunnelSpot = new Vector2D(i, j);
                    if (tunnels.containsKey(tunnelSpot) || tunnels.containsValue(tunnelSpot)){
                        Rectangle tunnelShape = new Rectangle(30, 30);
                        tunnelShape.setFill(Color.BLACK);
                        cell.getChildren().add(tunnelShape);
                    }
                }

                int column = i;
                int row = j;
                cell.setOnMouseClicked(event -> handleCellClick(row, column));

                GridPane.setHalignment(cell, HPos.CENTER);
                mapGrid.add(cell, i, j);
            }
        }
    }
    private String mapEnergyToColor(int energy, int referenceEnergy) {
        double energyRatio = (double) energy / referenceEnergy;

        if (energyRatio > 0.8) {
            return "green";
        } else if (energyRatio > 0.6) {
            return "limegreen";
        } else if (energyRatio > 0.4) {
            return "yellow";
        } else if (energyRatio > 0.2) {
            return "orange";
        } else {
            return "red";
        }
    }

    private void updateTrackingAnimal(Animal animal) {
        animalGenotype.setText("Genotyp zwierzaka: " + animal.getGenotype());

        int numberOfGenotype = animal.getDaysSurvived() % animal.getGenotype().size();

        animalActiveGenome.setText("Aktywny gen: " + animal.getGenotype().get(numberOfGenotype));
        animalEnergy.setText("Energia zwierzaka: " + animal.getEnergy());
        animalCountOfConsumedPlants.setText("Zjedzonych roslin: " + animal.getConsumedPlants());
        animalCountOfChildren.setText("Liczba dzieci: " + animal.getAmountOfCloseChildren());
        animalCountOfDescendant.setText("Liczba potomkow: " + animal.getAmountOfChildren());
        animalDaysSurvived.setText("Przetrwane dni: " + animal.getDaysSurvived());
        animalDayOfDeath.setText("Dzien smierci: ");
        if (animal.getEnergy() <= 0){
            animalDayOfDeath.setText("Dzien smierci: " + animal.getDayOfDeath());
        }
    }

    private void handleCellClick(int row, int column) {
        Vector2D pointedPosition = new Vector2D(column, row);

        for (Animal animal : animalArrayList) {
            if (Objects.equals(animal.position(), pointedPosition)){
                trackedAnimal = animal;
                updateTrackingAnimal(animal);
                break;
            }
        }
    }

    private synchronized void clearGrid() {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().get(0));
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }

    private ArrayList<Animal> generateAnimals(SimulationParameters simulationParameters) {
        int animalsAmountOnStart = animalsAmountOnStartLabel.getValue();
        ArrayList<Animal> animalList = new ArrayList<>();
        for (int i = 0; i < animalsAmountOnStart; i++){
            animalList.add(new Animal(simulationParameters));
        }
        return animalList;
    }

    @Override
    public void update(int animalsCount, int plantsCount, double averageEnergy,
                       double averageLifespan, int amountOfFreeSpots, int simulationDay, double averageNumberOfChildren, boolean isEnd, List<Integer> mostPopularGenotype) {
        Platform.runLater(() -> {
            this.simulationDayLabel.setText("Dzien symulacji: " + simulationDay);
            this.animalCountLabel.setText("Liczba zwierzat: " + animalsCount);
            this.plantCountLabel.setText("Liczba roslin: " + plantsCount);
            this.averageEnergyLabel.setText("Srednia energia: " + averageEnergy);
            this.averageLifespanLabel.setText("Srednia dlugosc zycia: " + averageLifespan);
            this.freeSpotsLabel.setText("Liczba wolnych pol: " + amountOfFreeSpots);
            this.mostPopularGenotypeLabel.setText("Najpopularniejszy genotyp: " + mostPopularGenotype);
            this.averageNumberOfChildrenLabel.setText("Srednia liczba dzieci: " + averageNumberOfChildren);

            if (this.saveToCSV)
            {
                csvWriter.println(simulationDay + "," + animalsCount + "," + plantsCount + "," +
                        averageEnergy + "," + averageLifespan + "," +
                        amountOfFreeSpots + "," + averageNumberOfChildren + "," + mostPopularGenotype);
                csvWriter.flush();
            }
            if (isEnd && presenter.saveToCSV) closeCSVWriter();
        });
    }

    public void closeCSVWriter() {
        if (csvWriter != null) {
            csvWriter.close();
        }
    }

    public void onStopTrackingClicked() {
        trackedAnimal = null;
        animalGenotype.setText("Genotyp zwierzaka: ");
        animalActiveGenome.setText("Aktywny gen: ");
        animalEnergy.setText("Energia zwierzaka: ");
        animalCountOfConsumedPlants.setText("Zjedzonych roslin: ");
        animalCountOfChildren.setText("Liczba dzieci: ");
        animalCountOfDescendant.setText("Liczba potomkow:");
        animalDaysSurvived.setText("Przetrwane dni: ");
        animalDayOfDeath.setText("Dzien smierci: ");
    }
}