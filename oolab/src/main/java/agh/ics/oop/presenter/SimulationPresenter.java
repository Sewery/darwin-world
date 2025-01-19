package agh.ics.oop.presenter;

import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationEngine;
import agh.ics.oop.core.Configuration;
import agh.ics.oop.core.Statistics;
import agh.ics.oop.model.GrassField;
import agh.ics.oop.model.GrassFieldWithPoles;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.WorldMap;
import agh.ics.oop.model.animal_life.Animal;
import agh.ics.oop.model.animal_life.AnimalComparator;
import agh.ics.oop.model.animal_life.AnimalChangeListener;
import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.MapChangeListener;
import agh.ics.oop.core.StatisticsChangeListener;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import lombok.Getter;
import lombok.Setter;
import lombok.val;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static java.lang.Math.max;


public class SimulationPresenter extends AppPresenter implements MapChangeListener, StatisticsChangeListener, AnimalChangeListener {
    private final int maxMapSize = 300;
    private final BooleanProperty isStartEnabled = new SimpleBooleanProperty(false);
    @FXML
    private  Label currentEnergy;
    @FXML
    private Label dayOfDeath;
    @FXML
    private CheckBox savingStatsToFile;
    @FXML
    private VBox highlightedAnimalVBox;
    @FXML
    private Label plantsEaten;
    @FXML
    private Label currentGene;
    @FXML
    private Label genome;
    @FXML
    private Label numberOfChildren;
    @FXML
    private Label numberOfDescendants;
    @FXML
    private Label age;
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
    @FXML
    private VBox chartContainer;
    @FXML
    private Button backButton;
    @FXML
    private Button numberOfAnimalsChartButton;
    @FXML
    private Button numberOfPlantsChartButton;
    @FXML
    private Button numberOfEmptySpacesChartButton;
    @FXML
    private Button averageEnergyChartButton;
    @FXML
    private Button averageLifespanChartButton;
    @FXML
    private Button averageNumberOfChildrenChartButton;
    @FXML
    private VBox generalStatisticsVBox;
    @FXML
    private LineChart<Number, Number> lineChart;

    private Image animalImage;
    private Image animalDominatingImage;
    private Image plantImage;
    private WorldMap worldMap;
    private Simulation simulation;
    private boolean isInitialized = false;
    private Animal highlightedAnimal = null;
    private Boundary boundary;
    private int minX;
    private int minY;
    private int cellsInARow;
    private int cellsInAColumn;
    private int cellSize = 20;
    private SimulationEngine engine = null;
    @Setter
    private Configuration configuration;
    public void setWorldMap(WorldMap worldMap) {
        this.worldMap = worldMap;
        if (!worldMap.getCurrentBounds().equals(boundary)) {
            setMapProperties(worldMap.getCurrentBounds());
        }
    }

    @FXML
    private void initialize() {
        stopButton.disableProperty().bind(isStartEnabled.not());
        startButton.disableProperty().bind(isStartEnabled);

        highlightedAnimalVBox.getChildren().forEach(child->child.setVisible(false));
        onBackButtonClicked();
    }

    private void setMapProperties(Boundary currentBounds) {
        boundary = currentBounds;
        minX = boundary.lowerLeft().getX();
        minY = boundary.lowerLeft().getY();
        Vector2d subtracted = boundary.upperRight().subtract(boundary.lowerLeft());
        cellsInARow = subtracted.getX();
        cellsInAColumn = subtracted.getY();

        cellSize = max(maxMapSize / (max(cellsInAColumn, cellsInARow) + 1), cellSize);
        renderImages((int) (cellSize * 0.7), (int) (cellSize * 0.7));
    }

    private void renderImages(int width, int height) {
        try (InputStream input = getClass().getResourceAsStream("/assets/grass.jpg")) {
            if (input == null) {
                throw new IOException("Resource not found: grass.jpg");
            }
            plantImage = new Image(input, width, height, true, true);
        } catch (IOException e) {
            System.err.println("Could not load image of grass: " + e.getMessage());
        }
        try (InputStream input = getClass().getResourceAsStream("/assets/sheep.jpg")) { // Corrected path
            if (input == null) {
                throw new IOException("Resource not found: sheep.jpg");
            }
            animalImage = new Image(input, width, height, true, true);
        } catch (IOException e) {
            System.err.println("Could not load image of animal: " + e.getMessage());
        }
        try (InputStream input = getClass().getResourceAsStream("/assets/sheep_crown.png")) { // Corrected path
            if (input == null) {
                throw new IOException("Resource not found: sheep_crown.jpg");
            }
            animalDominatingImage = new Image(input, width, height, true, true);
        } catch (IOException e) {
            System.err.println("Could not load image of dominating animal: " + e.getMessage());
        }
    }

    private void colourMap(int cellSize) {

        int equatorWidth = max(configuration.height() / 5, 1);
        int equatorUpperBound = (configuration.height() - 1) / 2 - (equatorWidth) / 2;
        if (equatorWidth % 2 == 0 && configuration.height() % 2 == 0) {
            equatorUpperBound += 1;
        }
        int equatorLowerBound = equatorUpperBound + equatorWidth - 1;


        for (int row = equatorUpperBound; row <= equatorLowerBound; row++) {
            for (int col = 0; col < cellsInARow + 1; col++) {
                Rectangle rect = new Rectangle(cellSize, cellSize);
                rect.setFill(Color.BEIGE);
                mapGrid.add(rect, col + 1, row + 1);
            }
        }

        if (this.configuration.mapStrategy() == Configuration.MapStrategy.POLES) {
            int h = 0;
            addRectsToGrid(
                    0,
                    equatorWidth,
                    Color.LIGHTBLUE
            );
            addRectsToGrid(
                    equatorWidth,
                    equatorWidth,
                    Color.LIGHTGREEN
            );
            addRectsToGrid(
                    equatorLowerBound + 1,
                    configuration.height() - equatorWidth,
                    Color.LIGHTGREEN
            );
            addRectsToGrid(
                    configuration.height() - equatorWidth,
                    configuration.height(),
                    Color.LIGHTBLUE
            );
//            for (int row = 0; row < equatorWidth; row++) {
//                for (int col = 0; col < cellsInARow + 1; col++) {
//                    Rectangle rect = new Rectangle(cellSize, cellSize);
//                    rect.setFill(Color.LIGHTBLUE);
//                    mapGrid.add(rect, col + 1, row + 1);
//                }
//            }
//            for (int row = equatorWidth; row < equatorUpperBound; row++) {
//                for (int col = 0; col < cellsInARow + 1; col++) {
//                    Rectangle rect = new Rectangle(cellSize, cellSize);
//                    rect.setFill(Color.LIGHTGREEN);
//                    mapGrid.add(rect, col + 1, row + 1);
//                }
//            }
//            for (int row = equatorLowerBound + 1; row <= configuration.height() - 1 - equatorWidth; row++) {
//                for (int col = 0; col < cellsInARow + 1; col++) {
//                    Rectangle rect = new Rectangle(cellSize, cellSize);
//                    rect.setFill(Color.LIGHTGREEN);
//                    mapGrid.add(rect, col + 1, row + 1);
//                }
//            }
//            for (int row = configuration.height() - 1; row > configuration.height() - 1 - equatorWidth; row--) {
//                for (int col = 0; col < cellsInARow + 1; col++) {
//                    Rectangle rect = new Rectangle(cellSize, cellSize);
//                    rect.setFill(Color.LIGHTBLUE);
//                    mapGrid.add(rect, col + 1, row + 1);
//                }
//            }
        } else {
            addRectsToGrid(
                    0,
                    equatorUpperBound,
                    Color.LIGHTGREEN
            );
            addRectsToGrid(
                    equatorLowerBound + 1,
                    configuration.height(),
                    Color.LIGHTGREEN
            );

//            for (int row = 0; row < equatorUpperBound; row++) {
//                for (int col = 0; col < cellsInARow + 1; col++) {
//                    Rectangle rect = new Rectangle(cellSize, cellSize);
//                    rect.setFill(Color.LIGHTGREEN);
//                    mapGrid.add(rect, col + 1, row + 1);
//                }
//            }
//
//            for (int row = equatorLowerBound + 1; row < configuration.height(); row++) {
//                for (int col = 0; col < cellsInARow + 1; col++) {
//                    Rectangle rect = new Rectangle(cellSize, cellSize);
//                    rect.setFill(Color.LIGHTGREEN);
//                    mapGrid.add(rect, col + 1, row + 1);
//                }
//            }


        }
    }
    private void addRectsToGrid(int rowStart,int rowEnd,Color color){
        for (int row = rowStart; row < rowEnd; row++) {
            for (int col = 0; col < cellsInARow + 1; col++) {
                Rectangle rect = new Rectangle(cellSize, cellSize);
                rect.setFill(color);
                mapGrid.add(rect, col + 1, row + 1);
            }
        }
    }


    private void drawMap() {

        colourMap(cellSize);
        // create grid
        mapGrid.getColumnConstraints().add(new ColumnConstraints(cellSize));
        for (int col = 0; col < cellsInARow + 1; col++) {
            mapGrid.getColumnConstraints().add(new ColumnConstraints(cellSize));
            Label label = new Label(Integer.toString(col + minX));
            GridPane.setHalignment(label, HPos.CENTER);
            mapGrid.add(label, col + 1, 0);
        }
        mapGrid.getRowConstraints().add(new RowConstraints(cellSize));
        for (int row = 0; row < cellsInAColumn + 1; row++) {
            mapGrid.getRowConstraints().add(new RowConstraints(cellSize));
            Label label = new Label(Integer.toString(cellsInAColumn - row + minY));
            GridPane.setHalignment(label, HPos.CENTER);
            mapGrid.add(label, 0, row + 1);
        }
        Label label = new Label("y/x");
        mapGrid.add(label, 0, 0);
        GridPane.setHalignment(label, HPos.CENTER);
        // add elements
        for (int x = 1; x < cellsInARow + 2; x++) {
            for (int y = cellsInAColumn + 1; y >= 0; y--) {

                Vector2d currentPosition = new Vector2d(x - 1 + minX, cellsInAColumn - y + minY);
                // zwierzęta
                List<Animal> animals = worldMap.animalsAt(currentPosition);
                if (animals != null) {
                    Animal animalToDraw = getStrongestAnimalOnPosition(animals);
                    drawAnimal(x, y, animalToDraw);

                }
                // trawa, jeśli nie ma zwierzęcia
                else if (worldMap.grassAt(currentPosition) != null) {
                    drawGrass(x, y);
                }
            }
        }

    }

    private void drawAnimal(int x, int y, Animal animal) {

        ColorAdjust colorAdjust = new ColorAdjust();

        //Dodaj niebiesla poswiate
        if (animal == highlightedAnimal) {
            colorAdjust.setHue(0.3);       // Red tint
            colorAdjust.setSaturation(0.8);
            //Dodaj bronzowa poswiate
        } else {
            colorAdjust.setHue(-0.05);
            colorAdjust.setSaturation(0.3);
        }
        String animalGenotype = Arrays.stream(animal.getGenotype()).boxed().toList().toString();
        ImageView animalView = new ImageView(animalImage);
        if(simulation.getMostCommonGenotypes().contains(animalGenotype)) {
            animalView = new ImageView(animalDominatingImage);
        }
        animalView.setEffect(colorAdjust);
        animalView.setOnMouseClicked(event -> {
            if (simulation.isPaused()) {
                this.highlightedAnimal = animal;
                animal.addObserver(this);
                highlightedAnimalVBox.setVisible(true);
                highlightedAnimalVBox.getChildren().forEach(child->child.setVisible(true));
                simulation.getStats().updateHighlightedAnimal(
                        animal.getGenotype(),
                        animal.getCurrentGene(),
                        animal.getEnergy(),
                        animal.getPlantsEaten(),
                        animal.getNumberOfChildren(),
                        animal.getNumberOfDescendants(),
                        animal.getAge()
                );
            }
        });

        AnchorPane healthBar = createHealthBar(animal.getEnergy(), simulation.getMaxEnergy());

        VBox mapElement = new VBox(animalView, healthBar);
        mapElement.setAlignment(Pos.CENTER);
        mapGrid.add(mapElement, x, y + 1);
        GridPane.setHalignment(mapElement, HPos.CENTER);

    }

    private void drawGrass(int x, int y) {
        //Label mapElement = new Label("*");
        ImageView plantView = new ImageView(plantImage);
        VBox mapElement = new VBox(plantView);
        mapElement.setAlignment(Pos.CENTER);
        mapGrid.add(mapElement, x, y + 1);
        GridPane.setHalignment(mapElement, HPos.CENTER);

    }

    private Animal getStrongestAnimalOnPosition(List<Animal> animals) {
        if (highlightedAnimal != null) {
            if (animals.contains(highlightedAnimal)) {
                return highlightedAnimal;
            }
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
        if (message.equals("Simulation paused")) {
            //todo
        }
        Platform.runLater(() -> {
            clearGrid();
            drawMap();
            moveDescription.setText(message);
        });
    }

    @Override
    public void statisticsChanged(Statistics statistics) {
        Platform.runLater(this::displayStatistics);
    }

    @FXML
    public void onSimulationStartClicked() throws IllegalArgumentException {
        savingStatsToFile.disableProperty().set(true);
        if (configuration != null) {

            if (!this.isInitialized) {

                WorldMap map = (configuration.mapStrategy() == Configuration.MapStrategy.POLES) ? new GrassFieldWithPoles(configuration) : new GrassField(configuration);
                Statistics statistics = new Statistics(configuration);
                Simulation simulation = new Simulation(map, configuration, statistics,savingStatsToFile.isSelected());
                this.simulation = simulation;
                map.addObserver(this);
                statistics.addObserver(this);
                statistics.notifyObservers();
                engine = new SimulationEngine(List.of(simulation));
                engine.runAsync();
                isStartEnabled.set(true);
                this.isInitialized = true;
            } else {
                if (engine != null) {
                    simulation.resumeSimulation();
                    isStartEnabled.set(true);
                    if (backButton.isVisible()) {onBackButtonClicked();}
                    numberOfAnimalsChartButton.setVisible(false);
                    numberOfPlantsChartButton.setVisible(false);
                    numberOfEmptySpacesChartButton.setVisible(false);
                    averageEnergyChartButton.setVisible(false);
                    averageLifespanChartButton.setVisible(false);
                    averageNumberOfChildrenChartButton.setVisible(false);
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
            });
        });

    }

    @FXML
    public void onSimulationStopClicked(ActionEvent actionEvent) throws InterruptedException {
        if (engine != null) {
            simulation.pauseSimulation();
            isStartEnabled.set(false);
            numberOfAnimalsChartButton.setVisible(true);
            numberOfPlantsChartButton.setVisible(true);
            numberOfEmptySpacesChartButton.setVisible(true);
            averageEnergyChartButton.setVisible(true);
            averageLifespanChartButton.setVisible(true);
            averageNumberOfChildrenChartButton.setVisible(true);
        }
    }


    private void displayStatistics() {
        val stats = simulation.getStats();
        numberOfAnimals.setText(stats.getNumberOfAllAnimals().getLast().toString());
        numberOfPlants.setText(stats.getNumberOfAllPlants().getLast().toString());
        numberOfEmptySpaces.setText(stats.getEmptySpaces().getLast().toString());
        mostCommonGenotypes.setText(stats.getMostPopularGenotypes().toString());
        List<String> currentMostPopularGenotypes = stats.getMostPopularGenotypes();
        if (currentMostPopularGenotypes.isEmpty()) {mostCommonGenotypes.setText("no animals on the map");}
        else {
            if (currentMostPopularGenotypes.size() > 1) {
                mostCommonGenotypes.setText("%s (%d more)".formatted(
                        currentMostPopularGenotypes.get(new Random().nextInt(currentMostPopularGenotypes.size())),
                        currentMostPopularGenotypes.size() - 1
                ));
            }
            else {mostCommonGenotypes.setText(currentMostPopularGenotypes.get(new Random().nextInt(currentMostPopularGenotypes.size())));}
        }
        averageEnergy.setText(stats.getAverageEnergy().getLast().toString());
        averageLifespan.setText(stats.getAverageLifespan().getLast().toString());
        averageNumberOfChildren.setText(simulation.getStats().getAverageNumberOfChildren().getLast().toString());
        if(highlightedAnimal!=null){
            genome.setText(stats.getGenome().toString());
            plantsEaten.setText(stats.getPlantsEaten().toString());
            currentGene.setText(stats.getCurrentGene().toString());
            currentEnergy.setText(stats.getCurrentEnergy().toString());
            numberOfChildren.setText(stats.getNumberOfChildren().toString());
            numberOfDescendants.setText(stats.getNumberOfDescendants().toString());
            age.setText(stats.getAge().toString());
            dayOfDeath.setText(stats.getDayOfDeath().toString());
        }

    }

    public void onNumberOfAnimalsChartButtonClicked() {
        showChart("Number of animals chart", simulation.getStats().getNumberOfAllAnimals());
    }
    public void onNumberOfPlantsChartButtonClicked() {
        showChart("Number of plants chart", simulation.getStats().getNumberOfAllPlants());
    }
    public void onNumberOfEmptySpacesChartButtonClicked() {
        showChart("Number of empty spaces chart", simulation.getStats().getEmptySpaces());
    }
    public void onAverageEnergyChartButtonClicked() {
        showChart("Average energy chart", simulation.getStats().getAverageEnergy());
    }
    public void onAverageLifespanChartButtonClicked() {
        showChart("Average lifespan chart", simulation.getStats().getAverageLifespan());
    }
    public void onAverageNumberOfChildrenChartButtonClicked() {
        showChart("Average number of children chart", simulation.getStats().getAverageNumberOfChildren());
    }

    private void showChart(String title, List<Integer> data){

        lineChart.getData().clear();

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName(title);

        for (int day = 0; day < data.size(); day++) {
            int population = data.get(day);
            series.getData().add(new XYChart.Data<>(day, population));
        }

        lineChart.getData().add(series);

        generalStatisticsVBox.setVisible(false);
        generalStatisticsVBox.setManaged(false);
        chartContainer.setVisible(true);
        chartContainer.setManaged(true);
    }

    public void onBackButtonClicked() {
        chartContainer.setVisible(false);
        chartContainer.setManaged(false);
        generalStatisticsVBox.setVisible(true);
        generalStatisticsVBox.setManaged(true);
    }

    @Override
    public void animalChanged(Animal animal,String message) {
        val stats = simulation.getStats();
        if(animal==highlightedAnimal){
            switch (message){
                case "numberOfDescendants"->stats.updateNumberOfDescendants(highlightedAnimal.getNumberOfDescendants());
                case "numberOfChildren"->stats.updateNumberOfChildren(highlightedAnimal.getNumberOfChildren());
                case "plantsEaten"->stats.updatePlantsEaten(highlightedAnimal.getPlantsEaten());
                case "currentGene"->stats.updateCurrentGene(highlightedAnimal.getNumberOfDescendants());
                case "age"->stats.updateAge(highlightedAnimal.getAge());
                case "isDead"->{
                    //Stop simulation
                    simulation.pauseSimulation();
                    isStartEnabled.set(false);
                    stats.updateDayOfDeath(highlightedAnimal.getDayOfDeath());
                    Platform.runLater(() -> {    new Alert(Alert.AlertType.INFORMATION, "Observed animal died", ButtonType.OK).show();});

                }
            }
            simulation.getStats().updateCurrentEnergy(animal.getEnergy());
        }
    }
    @FXML
    public void onStopObservingButtonClicked(ActionEvent actionEvent) {
        if(highlightedAnimal != null) {
            highlightedAnimalVBox.getChildren().forEach(child->child.setVisible(false));
            highlightedAnimalVBox.setVisible(false);
            highlightedAnimal.removeObserver(this);
            simulation.getStats().resetHighlightedAnimal();
            this.highlightedAnimal = null;
        }
    }
}