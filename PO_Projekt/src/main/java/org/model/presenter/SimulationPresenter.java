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
    public void setWorldMap(Board worldMap) {
        this.worldMap = worldMap;
    }
    private String[] defaultSettings = {"Easy", "Hard", "Endless Simulation"};
    private String selectedOption;
    private HashMap<String, String[]> settings = new HashMap<>();
    private SimulationPresenter presenter;
    private PrintWriter csvWriter;

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

            settings.put("Easy", new String[]{"5", "5", "0", "2", "15", "5", "3", "1", "5", "15", "3", "25", "40", "0"});
            settings.put("Hard", new String[]{"10", "10", "2", "5", "5", "1", "2", "2", "10", "10", "5", "30", "30", "0"});
            settings.put("Endless Simulation", new String[]{"5", "5", "2", "3", "20", "10", "4", "0", "7", "15", "4", "30", "50", "0"});
            defaultConfigurationsListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    selectedOption = defaultConfigurationsListView.getSelectionModel().getSelectedItem();

                    String[] parameters = settings.get(selectedOption);

                    minReproduceEnergyLabel.setText(parameters[0]);
                    energyLostOnReproductionLabel.setText(parameters[1]);
                    minMutationsLabel.setText(parameters[2]);
                    maxMutationsLabel.setText(parameters[3]);
                    startingAmountOfPlantsLabel.setText(parameters[4]);
                    newPlantPerDayLabel.setText(parameters[5]);
                    plantEnergyLabel.setText(parameters[6]);
                    energyLostPerDayLabel.setText(parameters[7]);
                    genotypeSizeLabel.setText(parameters[8]);
                    startingAnimalEnergyLabel.setText(parameters[9]);
                    animalsAmountOnStartLabel.setText(parameters[10]);
                    mapHeightLabel.setText(parameters[11]);
                    mapWidthLabel.setText(parameters[12]);
                    numberOfTunnelsLabel.setText(parameters[13]);

                }
            });
        }
    }

    @FXML
    public void onSaveConfiguration(){
        String[] savedSettings = {
                minReproduceEnergyLabel.getText(),
                energyLostOnReproductionLabel.getText(),
                minMutationsLabel.getText(),
                maxMutationsLabel.getText(),
                startingAmountOfPlantsLabel.getText(),
                newPlantPerDayLabel.getText(),
                plantEnergyLabel.getText(),
                energyLostPerDayLabel.getText(),
                genotypeSizeLabel.getText(),
                startingAnimalEnergyLabel.getText(),
                animalsAmountOnStartLabel.getText(),
                mapHeightLabel.getText(),
                mapWidthLabel.getText(),
                numberOfTunnelsLabel.getText()
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

        createSimulationStage(map, engine);

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

    private void createSimulationStage(Board map, SimulationEngine engine) throws IOException {
        Stage simulationStage = new Stage();
        simulationStage.setTitle("Running simulation");

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("simulation.fxml"));
        BorderPane viewRoot = loader.load();
        Scene scene = new Scene(viewRoot);

        SimulationPresenter presenter = loader.getController();
        this.presenter = presenter;
        presenter.setWorldMap(map);

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
        int animalsAmountOnStart = Integer.parseInt(animalsAmountOnStartLabel.getText());
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