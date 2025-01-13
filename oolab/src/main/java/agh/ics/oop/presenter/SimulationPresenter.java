package agh.ics.oop.presenter;

import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationEngine;
import agh.ics.oop.core.AppState;
import agh.ics.oop.core.Configuration;
import agh.ics.oop.core.Statistics;
import agh.ics.oop.model.*;
import agh.ics.oop.model.animal_life.Animal;
import agh.ics.oop.model.animal_life.AnimalComparator;
import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.MapChangeListener;
import agh.ics.oop.model.util.StatisticsChangeListener;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.lang.Math.max;
import static java.lang.Math.multiplyHigh;


public class SimulationPresenter extends AppPresenter implements MapChangeListener, StatisticsChangeListener {
    @FXML
    private TextField movesList;
    @FXML
    private Label moveDescription;
    @FXML
    private GridPane mapGrid;
    @FXML
    private Label numberOfAnimals;
    @FXML
    private Label numberOfPlants;
    @FXML
    private Label numberOfEmptySpaces;
    @FXML
    private Label mostCommonGenotypes;
    @FXML
    private Label averageEnergy;
    @FXML
    private Label averageLifespan;
    @FXML
    private Label averageNumberOfChildren;
    @FXML
    private Button stopButton;
    @FXML
    private Button startButton;

    private WorldMap worldMap;
    private Simulation simulation;
    private Configuration configuration;
    private Statistics statistics;
    private boolean isInitialized=false;
    private Animal highlightedAnimal = null;

    private Boundary boundary;
    private int minX;
    private int minY;
    private int cellsInARow;
    private int cellsInAColumn;

    private final int maxMapSize = 300;
    private int cellSize = 20;
    private SimulationEngine engine=null;

    private final BooleanProperty isStartDisabled = new SimpleBooleanProperty(false);
    public void setWorldMap(WorldMap worldMap) {
        this.worldMap = worldMap;
    }

    @FXML
    private void initialize() {
        stopButton.disableProperty().bind(isStartDisabled.not());
        startButton.disableProperty().bind(isStartDisabled);
    }
    private void colourMap(int cellSize){

        int equatorWidth = max(configuration.height()/5, 1);
        int equatorUpperBound = (configuration.height()-1)/2 - (equatorWidth)/2;
        if (equatorWidth % 2 == 0 && configuration.height() % 2 == 0){equatorUpperBound += 1;}
        int equatorLowerBound = equatorUpperBound + equatorWidth - 1;


        for (int row = equatorUpperBound; row <= equatorLowerBound; row++) {
           for (int col = 0; col < cellsInARow + 1; col++) {
                Rectangle rect = new Rectangle(cellSize, cellSize);
                rect.setFill(Color.BEIGE);
                mapGrid.add(rect, col + 1, row +1);
            }
        }

        if (this.configuration.mapStrategy() == Configuration.MapStrategy.POLES) {
            int h = 0;
            for (int row = 0; row < equatorWidth; row++) {
                for (int col = 0; col < cellsInARow + 1; col++) {
                    Rectangle rect = new Rectangle(cellSize, cellSize);
                    rect.setFill(Color.LIGHTBLUE);
                    mapGrid.add(rect, col + 1, row +1);
                }
            }
            for (int row = equatorWidth; row < equatorUpperBound; row++) {
                for (int col = 0; col < cellsInARow + 1; col++) {
                    Rectangle rect = new Rectangle(cellSize, cellSize);
                    rect.setFill(Color.LIGHTGREEN);
                    mapGrid.add(rect, col + 1, row +1);
                }
            }
            for (int row = equatorLowerBound + 1; row <= configuration.height() - 1 - equatorWidth; row++) {
                for (int col = 0; col < cellsInARow + 1; col++) {
                    Rectangle rect = new Rectangle(cellSize, cellSize);
                    rect.setFill(Color.LIGHTGREEN);
                    mapGrid.add(rect, col + 1, row +1);
                }
            }
            for (int row = configuration.height() - 1; row >  configuration.height() - 1 - equatorWidth; row--) {
                for (int col = 0; col < cellsInARow + 1; col++) {
                    Rectangle rect = new Rectangle(cellSize, cellSize);
                    rect.setFill(Color.LIGHTBLUE);
                    mapGrid.add(rect, col + 1, row +1);
                }
            }
        }
        else{

            for (int row = 0; row < equatorUpperBound; row++) {
                for (int col = 0; col < cellsInARow + 1; col++) {
                    Rectangle rect = new Rectangle(cellSize, cellSize);
                    rect.setFill(Color.LIGHTGREEN);
                    mapGrid.add(rect, col + 1, row +1);
                }
            }

            for (int row = equatorLowerBound + 1; row < configuration.height(); row++) {
                for (int col = 0; col < cellsInARow + 1; col++) {
                    Rectangle rect = new Rectangle(cellSize, cellSize);
                    rect.setFill(Color.LIGHTGREEN);
                    mapGrid.add(rect, col + 1, row + 1);
                }
            }


        }
    }
    private void drawMap() {

        boundary = worldMap.getCurrentBounds();
        minX = boundary.lowerLeft().getX();
        minY = boundary.lowerLeft().getY();
        Vector2d subtracted = boundary.upperRight().subtract(boundary.lowerLeft());
        cellsInARow = subtracted.getX();
        cellsInAColumn = subtracted.getY();

        cellSize = max(maxMapSize/(max(cellsInAColumn, cellsInARow)+1), cellSize);

        colourMap(cellSize);


        // create grid
        mapGrid.getColumnConstraints().add(new ColumnConstraints(cellSize));
        for (int col = 0; col < cellsInARow+1; col++) {
            mapGrid.getColumnConstraints().add(new ColumnConstraints(cellSize));
            Label label = new Label(Integer.toString(col+minX));
            GridPane.setHalignment(label, HPos.CENTER);
            mapGrid.add(label, col+1, 0);
        }
        mapGrid.getRowConstraints().add(new RowConstraints(cellSize));
        for (int row = 0; row < cellsInAColumn+1; row++) {
            mapGrid.getRowConstraints().add(new RowConstraints(cellSize));
            Label label = new Label(Integer.toString(cellsInAColumn-row+minY));
            GridPane.setHalignment(label, HPos.CENTER);
            mapGrid.add(label, 0, row+1);
        }
        Label label = new Label("y/x");
        mapGrid.add(label, 0, 0);
        GridPane.setHalignment(label, HPos.CENTER);

        int maxEnergy = simulation.getMaxEnergy();
        // add elements
        for (int x = 1; x < cellsInARow+2; x++) {
            for (int y = cellsInAColumn+1; y >= 0; y--) {

                Vector2d currentPosition = new Vector2d(x-1+minX, cellsInAColumn-y+minY);
                // zwierzęta
                List<Animal> animals = worldMap.animalsAt(currentPosition);
                if (animals != null){

                    Animal animalToDraw = getStrongestAnimalOnPosition(animals);
                    Rectangle animalSymbol = new Rectangle(cellSize/2, cellSize/2);

                    if (animalToDraw == highlightedAnimal){
                        animalSymbol.setFill(Color.YELLOW);
                    }
                    else {
                        animalSymbol.setFill(Color.BROWN);
                    }

                    animalSymbol.setOnMouseClicked(event -> {
                        if (simulation.isPaused()) {
                            this.highlightedAnimal = animalToDraw;
                            worldMap.notifyObservers("Highlighted an animal");
                        }
                    });

                    AnchorPane healthBar = createHealthBar(animalToDraw.getEnergy(), maxEnergy);

                    VBox mapElement = new VBox(animalSymbol, healthBar);
                    mapElement.setAlignment(Pos.CENTER);
                    mapGrid.add(mapElement, x, y + 1);
                    GridPane.setHalignment(mapElement, HPos.CENTER);
                }

                // trawa, jeśli nie ma zwierzęcia
                else if (worldMap.grassAt(currentPosition) != null) {
                    Label mapElement = new Label("*");
                    mapGrid.add(mapElement, x, y+1);
                    GridPane.setHalignment(mapElement, HPos.CENTER);
                }



            }
        }

    }

    private Animal getStrongestAnimalOnPosition(List<Animal> animals) {
        if (highlightedAnimal != null) {
            if (animals.contains(highlightedAnimal)) {
                return highlightedAnimal;}
        }
        animals.sort(new AnimalComparator());
        return animals.getFirst();
    }
    private AnchorPane createHealthBar(int currentEnergy, int maxEnergy) {
        int width = cellSize * 7 / 10;
        int height = cellSize / 10;

        Rectangle background = new Rectangle(width, height);
        background.setFill(Color.DARKGRAY);
        background.setArcWidth(height);
        background.setArcHeight(height);

        double healthPercentage = (double) currentEnergy / maxEnergy;
        Rectangle health = new Rectangle(width * healthPercentage, height);
        if (healthPercentage > 0.5) {
            health.setFill(Color.GREEN);
        } else if (healthPercentage > 0.1) {
            health.setFill(Color.ORANGE);
        } else {
            health.setFill(Color.RED);
        }
        health.setArcWidth(height);
        health.setArcHeight(height);

        Rectangle border = new Rectangle(width, height);
        border.setFill(Color.TRANSPARENT);
        border.setStroke(Color.BLACK);
        border.setStrokeWidth(height / 5);
        border.setArcWidth(height);
        border.setArcHeight(height);


        AnchorPane healthBar = new AnchorPane();
        healthBar.setPrefSize(width, height);
        healthBar.getChildren().add(background);
        AnchorPane.setTopAnchor(background, 0.0);
        AnchorPane.setLeftAnchor(background, 0.0);
        healthBar.getChildren().add(health);
        AnchorPane.setTopAnchor(health, 0.0);
        AnchorPane.setLeftAnchor(health, 0.0);
        healthBar.getChildren().add(border);
        AnchorPane.setTopAnchor(border, 0.0);
        AnchorPane.setLeftAnchor(border, 0.0);

        return healthBar;
    }
    private void clearGrid() {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().getFirst()); // hack to retain visible grid lines
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }

    @Override
    public void mapChanged(WorldMap worldMap, String message) {
        setWorldMap(worldMap);
        if (Objects.equals(message, "Simulation paused")) { this.highlightedAnimal = null; }
        Platform.runLater(() -> {
            clearGrid();
            drawMap();
            moveDescription.setText(message);
        });
    }

    @FXML
    public void onSimulationStartClicked() throws IllegalArgumentException {
        if(configuration!=null){

            if (!this.isInitialized) {

                WorldMap map = (configuration.mapStrategy() == Configuration.MapStrategy.POLES) ? new GrassFieldWithPoles(configuration) : new GrassField(configuration);

                Statistics statistics = new Statistics(configuration);
                Simulation simulation = new Simulation(map, configuration, statistics);
                this.simulation = simulation;
                map.addObserver(this);
                statistics.addObserver(this);
                statistics.notifyObservers();
                engine = new SimulationEngine(List.of(simulation));
                engine.runAsync();
                isStartDisabled.set(true);
                this.isInitialized = true;
            }

            else {
                if(engine!=null){
                    simulation.resumeSimulation();
                    isStartDisabled.set(true);
                }
            }
        }

        Platform.runLater(() -> {
            mapGrid.getScene().getWindow().setOnCloseRequest(event -> {
                simulation.stopSimulation();
                try {
                    engine.awaitSimulationsEnd();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                Platform.exit();
            });
        });

    }

    @FXML
    public void onSimulationStopClicked(ActionEvent actionEvent) throws InterruptedException {
        if(engine!=null){
            simulation.pauseSimulation();
            isStartDisabled.set(false);
        }
    }


    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }

    @Override
    public void statisticsChanged(Statistics statistics) {
        setStatistics(statistics);
        Platform.runLater(this::displayStatistics);
    }

    private void displayStatistics() {

        numberOfAnimals.setText(statistics.getNumberOfAllAnimals().getLast().toString());
        numberOfPlants.setText(statistics.getNumberOfAllPlants().getLast().toString());
        numberOfEmptySpaces.setText(statistics.getEmptySpaces().getLast().toString());
        mostCommonGenotypes.setText(statistics.getMostPopularGenotypes().toString());
        averageEnergy.setText(statistics.getAverageEnergy().getLast().toString());
        averageLifespan.setText(statistics.getAverageLifespan().getLast().toString());
        averageNumberOfChildren.setText(statistics.getAverageNUmberOfChildren().getLast().toString());
    }

}
