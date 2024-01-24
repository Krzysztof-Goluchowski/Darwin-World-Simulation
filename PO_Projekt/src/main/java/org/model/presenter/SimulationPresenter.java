package org.model.presenter;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.model.*;
import org.model.util.ConsoleMapDisplay;
import javafx.stage.Screen;
import javafx.geometry.Rectangle2D;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.*;


public class SimulationPresenter implements Initializable, SimulationObserver {

    @FXML
    private Spinner startingAmountOfPlantsLabel;
    @FXML
    private Spinner minReproduceEnergyLabel;
    @FXML
    private Spinner energyLostOnReproductionLabel;
    @FXML
    private Spinner minMutationsLabel;
    @FXML
    private Spinner maxMutationsLabel;
    @FXML
    private Spinner newPlantPerDayLabel;
    @FXML
    private Spinner energyLostPerDayLabel;
    @FXML
    private Spinner genotypeSizeLabel;
    @FXML
    private Spinner plantEnergyLabel;
    @FXML
    private Spinner startingAnimalEnergyLabel;
    @FXML
    private Spinner mapHeightLabel;
    @FXML
    private Spinner mapWidthLabel;
    @FXML
    private Spinner animalsAmountOnStartLabel;
    @FXML
    private Spinner numberOfTunnelsLabel;
    @FXML
    private ComboBox<String> mutationVariantComboBox;
    @FXML
    private ComboBox<String> mapVariantComboBox;
    @FXML
    private GridPane mapGrid;
    @FXML
    private Label animalCountLabel;
    @FXML
    private Label plantCountLabel;
    @FXML
    private Label averageEnergyLabel;
    @FXML
    private Label averageLifespanLabel;
    @FXML
    private Label freeSpotsLabel;
    @FXML
    private Label mostPopularGenotypeLabel;
    @FXML
    public Label averageNumberOfChildrenLabel;
    @FXML
    public Label simulationDayLabel;
    @FXML
    private ListView<String> defaultConfigurationsListView;
    @FXML
    private CheckBox createCSVCheckBox;
    private SimulationEngine engine;
    private Board worldMap;
    private String[] defaultSettings = {"Easy", "Hard", "Endless Simulation"};
    private String selectedOption;
    private HashMap<String, int[]> settings = new HashMap<>();
    private SimulationPresenter presenter;
    private PrintWriter csvWriter;

    private SimulationParameters simulationParameters;

    public void setWorldMap(Board worldMap) {
        this.worldMap = worldMap;
    }

    public void setSimulationParameters(SimulationParameters simulationParameters){
        this.simulationParameters = simulationParameters;
    }

    public SimulationPresenter() {
        try {
            // Utworzenie i otwarcie pliku do zapisu
            FileWriter fileWriter = new FileWriter("simulation_stats.csv", true); // Ustaw true dla zachowania danych przy każdym uruchomieniu
            csvWriter = new PrintWriter(fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (defaultConfigurationsListView != null) {

            defaultConfigurationsListView.getItems().addAll(defaultSettings);

            settings.put("Easy", new int[]{5, 5, 0, 2, 15, 5, 3, 1, 5, 15, 3, 0, 0, 25, 40, 0});
            settings.put("Hard", new int[]{10, 10, 2, 5, 5, 1, 2, 2, 10, 10, 5, 0, 0, 30, 30, 0});
            settings.put("Endless Simulation", new int[]{5, 5, 2, 3, 20, 10, 4, 0, 7, 15, 4, 0, 0, 30, 50, 0});
            defaultConfigurationsListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    selectedOption = defaultConfigurationsListView.getSelectionModel().getSelectedItem();

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
                }
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
                (int) minReproduceEnergyLabel.getValue(),
                (int) energyLostOnReproductionLabel.getValue(),
                (int) minMutationsLabel.getValue(),
                (int) maxMutationsLabel.getValue(),
                (int) startingAmountOfPlantsLabel.getValue(),
                (int) newPlantPerDayLabel.getValue(),
                (int) plantEnergyLabel.getValue(),
                (int) energyLostPerDayLabel.getValue(),
                (int) genotypeSizeLabel.getValue(),
                (int) startingAnimalEnergyLabel.getValue(),
                (int) animalsAmountOnStartLabel.getValue(),
                map,
                mutation,
                (int) mapHeightLabel.getValue(),
                (int) mapWidthLabel.getValue(),
                (int) numberOfTunnelsLabel.getValue()

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
    }

    @FXML
    public void onResumeClicked() {
        engine.resumeAllSimulations();
    }
    @FXML
    public void onSimulationStartClicked() throws IOException {
        SimulationParameters simulationParameters = getParameters();

        ArrayList<Animal> animalList = generateAnimals(simulationParameters);

        Board map = configureMap(simulationParameters);

        Simulation simulation = new Simulation(simulationParameters, map, animalList);
        simulation.addObserver(this);
        SimulationEngine engine = new SimulationEngine(List.of(simulation));

        createSimulationStage(map, engine, simulationParameters);

        engine.runAsync();
    }

    private Board configureMap(SimulationParameters simulationParameters) {
        int mapHeight = (int) mapHeightLabel.getValue();
        int mapWidth = (int) mapWidthLabel.getValue();
        int numberOfTunnels = (int) numberOfTunnelsLabel.getValue();

        return createMap(simulationParameters.getMapVariant(), mapWidth, mapHeight, numberOfTunnels);
    }

    private Board createMap(SimulationParameters.MapVariant mapVariant, int mapWidth, int mapHeight, int numberOfTunnels) {
        if (mapVariant == SimulationParameters.MapVariant.TUNNELS) {
            return new BoardWithTunnels(mapWidth, mapHeight, numberOfTunnels);
        } else {
            return new Board(mapWidth, mapHeight);
        }
    }

    private void createSimulationStage(Board map, SimulationEngine engine, SimulationParameters simulationParameters) throws IOException {


        Stage simulationStage = new Stage();
        simulationStage.setTitle("Running simulation");

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("simulation.fxml"));
        BorderPane viewRoot = loader.load();
        Scene scene = new Scene(viewRoot);

        SimulationPresenter presenter = loader.getController();
        this.presenter = presenter;
        presenter.setWorldMap(map);

        presenter.setSimulationParameters(simulationParameters);

        presenter.setEngine(engine);

        ConsoleMapDisplay observer = new ConsoleMapDisplay(presenter);
        map.setObserver(observer);

        setSimulationStageSize(simulationStage);

        simulationStage.setScene(scene);
        simulationStage.show();
        presenter.drawMap();
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
        int startingAmountOfPlants = (int) startingAmountOfPlantsLabel.getValue();
        int minReproduceEnergy = (int) minReproduceEnergyLabel.getValue();
        int energyLostOnReproduction = (int) energyLostOnReproductionLabel.getValue();
        int minMutations = (int) minMutationsLabel.getValue();
        int maxMutations = (int) maxMutationsLabel.getValue();
        int newPlantsPerDay = (int) newPlantPerDayLabel.getValue();
        int energyLostPerDay = (int) energyLostPerDayLabel.getValue();
        int genotypeSize = (int) genotypeSizeLabel.getValue();
        int plantEnergy = (int) plantEnergyLabel.getValue();
        int startingAnimalEnergy = (int) startingAnimalEnergyLabel.getValue();

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

    public void drawMap() {
        clearGrid();

        for (int i = 0; i < worldMap.getWidth(); i++) {
            mapGrid.getColumnConstraints().add(new ColumnConstraints(30));
        }

        for (int j = 0; j < worldMap.getHeight(); j++) {
            mapGrid.getRowConstraints().add(new RowConstraints(30));
        }

        for (int i = 0; i < worldMap.getWidth(); i++) {
            for (int j = 0; j < worldMap.getHeight(); j++) {
                StackPane cell = new StackPane(); // Używamy StackPane, aby umożliwić wyświetlenie kształtów
                Object mapObject = worldMap.objectAt(new Vector2D(i, j));

                if (mapObject instanceof Animal) {
                    Animal animal = (Animal) mapObject;
                    Rectangle animalShape = new Rectangle(30, 30);
                    animalShape.setFill(Color.web(mapEnergyToColor(animal.getEnergy(), 10)));
                    cell.getChildren().add(animalShape);
                } else if (mapObject instanceof Plant) {
                    Circle plantShape = new Circle(15); // Promień połowy szerokości komórki
                    plantShape.setFill(Color.GREEN);
                    cell.getChildren().add(plantShape);
                }

                if (simulationParameters.getMapVariant() == SimulationParameters.MapVariant.TUNNELS){
                    Map<Vector2D, Vector2D> tunnels = worldMap.getTunnelsMaps();
                    Vector2D tunnelSpot = new Vector2D(i, j);
                    if (tunnels.containsKey(tunnelSpot) || tunnels.containsValue(tunnelSpot)){
                        Rectangle tunnelShape = new Rectangle(30, 30);
                        tunnelShape.setFill(Color.BLACK);
                        cell.getChildren().add(tunnelShape);
                    }
                }

                GridPane.setHalignment(cell, HPos.CENTER);
                mapGrid.add(cell, i, j);
            }
        }
    }

    private String mapEnergyToColor(int energy, int referenceEnergy) {
        double energyRatio = (double) energy / referenceEnergy;

        if (energyRatio > 0.8) {
            return "green"; // Najwyższy poziom energii
        } else if (energyRatio > 0.6) {
            return "limegreen"; // Wysoki poziom energii
        } else if (energyRatio > 0.4) {
            return "yellow"; // Średni poziom energii
        } else if (energyRatio > 0.2) {
            return "orange"; // Niski poziom energii
        } else {
            return "red"; // Bardzo niski poziom energii
        }
    }

    private void clearGrid() {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().get(0)); // hack to retain visible grid lines
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }

    private ArrayList<Animal> generateAnimals(SimulationParameters simulationParameters) {
        int animalsAmountOnStart = (int) animalsAmountOnStartLabel.getValue();
        ArrayList<Animal> animalList = new ArrayList<>();
        for (int i = 0; i < animalsAmountOnStart; i++){
            animalList.add(new Animal(simulationParameters));
        }
        return animalList;
    }

    @Override
    public void update(int animalsCount, int plantsCount, double averageEnergy,
                       double averageLifespan, int amountOfFreeSpots, int simulationDay, double averageNumberOfChildren, boolean isEnd) {
        Platform.runLater(() -> {
            presenter.simulationDayLabel.setText("Dzien symulacji: " + simulationDay);
            presenter.animalCountLabel.setText("Liczba zwierzat: " + animalsCount);
            presenter.plantCountLabel.setText("Liczba roslin: " + plantsCount);
            presenter.averageEnergyLabel.setText("Srednia energia: " + averageEnergy);
            presenter.averageLifespanLabel.setText("Srednia dlugosc zycia: " + averageLifespan);
            presenter.freeSpotsLabel.setText("Liczba wolnych pol: " + amountOfFreeSpots);
            presenter.mostPopularGenotypeLabel.setText("Najpopularniejszy genotyp: ");
            presenter.averageNumberOfChildrenLabel.setText("Srednia liczba dzieci: " + averageNumberOfChildren);

            csvWriter.println(simulationDay + "," + animalsCount + "," + plantsCount + "," +
                    averageEnergy + "," + averageLifespan + "," +
                    amountOfFreeSpots + "," + averageNumberOfChildren);
            csvWriter.flush();

            if (isEnd) closeCSVWriter();
        });
    }

    public void closeCSVWriter() {
        if (csvWriter != null) {
            csvWriter.close();
        }
    }
}