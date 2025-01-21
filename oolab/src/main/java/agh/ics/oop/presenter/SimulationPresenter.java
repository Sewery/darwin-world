package agh.ics.oop.presenter;

import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationEngine;
import agh.ics.oop.core.Configuration;
import agh.ics.oop.core.Statistics;
import agh.ics.oop.core.StatisticsChangeListener;
import agh.ics.oop.model.GrassField;
import agh.ics.oop.model.GrassFieldWithPoles;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.WorldMap;
import agh.ics.oop.model.animal_life.Animal;
import agh.ics.oop.model.animal_life.AnimalChangeListener;
import agh.ics.oop.model.animal_life.AnimalComparator;
import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.MapChangeListener;
import agh.ics.oop.presenter.view_components.HealthBarView;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import lombok.Setter;
import lombok.val;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static java.lang.Math.max;


public class SimulationPresenter extends AppPresenter implements MapChangeListener, StatisticsChangeListener, AnimalChangeListener {
    @FXML
    private Label currentEnergy;
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
    private final BooleanProperty isStartEnabled = new SimpleBooleanProperty(false);

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
    private final HealthBarView healthBarView = new HealthBarView();

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

        int maxMapSize = 300;
        cellSize = max(maxMapSize / (max(cellsInAColumn, cellsInARow) + 1), cellSize);
        loadImages((int) (cellSize * 0.7), (int) (cellSize * 0.7));
    }
    private void loadImages(int width, int height) {
        plantImage = loadImageFromAssets(width,height,"grass.jpg");
        animalImage =loadImageFromAssets(width,height,"sheep.jpg");
        animalDominatingImage= loadImageFromAssets(width,height,"sheep_crown.png");
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
            addRectsToMapGrid(0, equatorWidth, Color.LIGHTBLUE);
            addRectsToMapGrid(equatorWidth, equatorUpperBound, Color.LIGHTGREEN);
            addRectsToMapGrid(equatorLowerBound + 1, configuration.height() - equatorWidth, Color.LIGHTGREEN);
            addRectsToMapGrid(configuration.height() - equatorWidth, configuration.height(), Color.LIGHTBLUE);

        } else {
            addRectsToMapGrid(0, equatorUpperBound, Color.LIGHTGREEN);
            addRectsToMapGrid(equatorLowerBound + 1, configuration.height(), Color.LIGHTGREEN);
        }
    }
    private void addRectsToMapGrid(int rowStart,int rowEnd,Color color){
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
                // Animals
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

        //Dodaj zoltawa poswiate
        if (animal == highlightedAnimal) {
            colorAdjust.setHue(0.3);       // Red tint
            colorAdjust.setSaturation(0.8);
        //Dodaj zoltawa rozowa poswiate
        } else {
            colorAdjust.setHue(-0.05);
            colorAdjust.setSaturation(0.3);
        }
        String animalGenotype = Arrays.stream(animal.getGenotype()).boxed().toList().toString();
        ImageView animalView = drawAnimalImage(animal, animalGenotype, colorAdjust);

        AnchorPane healthBar = healthBarView.createHealthBar(animal.getEnergy(), simulation.getMaxEnergy(), cellSize);

        VBox mapElement = new VBox(animalView, healthBar);
        mapElement.setAlignment(Pos.CENTER);
        mapGrid.add(mapElement, x, y + 1);
        GridPane.setHalignment(mapElement, HPos.CENTER);

    }

    private ImageView drawAnimalImage(Animal animal, String animalGenotype, ColorAdjust colorAdjust) {
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
                worldMap.notifyObservers("Animal highlighted");
            }
        });
        return animalView;
    }

    private void drawGrass(int x, int y) {
        ImageView plantView = new ImageView(plantImage);
        VBox mapElement = new VBox(plantView);
        mapElement.setAlignment(Pos.CENTER);
        mapGrid.add(mapElement, x, y + 1);
        GridPane.setHalignment(mapElement, HPos.CENTER);
    }

    private Animal getStrongestAnimalOnPosition(List<Animal> animals) {
        if (highlightedAnimal != null) {
            if (animals.contains(highlightedAnimal)) {
                return highlightedAnimal;}}
        animals.sort(new AnimalComparator());
        return animals.getFirst();
    }

    private void clearGrid() {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().getFirst()); // hack to retain visible grid lines
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }

    @Override
    public void mapChanged(WorldMap worldMap, String message) {
        setWorldMap(worldMap);
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
    public void onSimulationStartClicked() {
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
                    alertError(e);
                    throw new RuntimeException(e);
                }
            });
        });

    }

    @FXML
    public void onSimulationStopClicked(ActionEvent actionEvent) {
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
        numberOfAnimals.setText(String.valueOf(stats.getNumberOfAllAnimals()));
        numberOfPlants.setText(String.valueOf(stats.getNumberOfAllPlants()));
        numberOfEmptySpaces.setText(String.valueOf(stats.getEmptySpaces()));
        mostCommonGenotypes.setText(stats.getMostPopularGenotypes().toString());
        List<String> currentMostPopularGenotypes = stats.getMostPopularGenotypes();
        if (currentMostPopularGenotypes.isEmpty()) {mostCommonGenotypes.setText("no animals on map");}
        else {
            if (currentMostPopularGenotypes.size() > 1) {
                mostCommonGenotypes.setText("%s (%d more)".formatted(
                        currentMostPopularGenotypes.get(new Random().nextInt(currentMostPopularGenotypes.size())),
                        currentMostPopularGenotypes.size() - 1
                ));
            }
            else {mostCommonGenotypes.setText(currentMostPopularGenotypes.get(new Random().nextInt(currentMostPopularGenotypes.size())));}
        }
        averageEnergy.setText(String.valueOf(stats.getAverageEnergy()));
        averageLifespan.setText(String.valueOf(stats.getAverageLifespan()));
        averageNumberOfChildren.setText(String.valueOf(simulation.getStats().getAverageNumberOfChildren()));

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
        showChart("Number of animals chart", simulation.getStats().getDailyNumberOfAllAnimals());}
    public void onNumberOfPlantsChartButtonClicked() {
        showChart("Number of plants chart", simulation.getStats().getDailyNumberOfAllPlants());}
    public void onNumberOfEmptySpacesChartButtonClicked() {
        showChart("Number of empty spaces chart", simulation.getStats().getDailyEmptySpaces());}
    public void onAverageEnergyChartButtonClicked() {
        showChart("Average energy chart", simulation.getStats().getDailyAverageEnergy());}
    public void onAverageLifespanChartButtonClicked() {
        showChart("Average lifespan chart", simulation.getStats().getDailyAverageLifespan());}
    public void onAverageNumberOfChildrenChartButtonClicked() {
        showChart("Average number of children chart", simulation.getStats().getDailyAverageNumberOfChildren());}

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
    @FXML
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
                    Platform.runLater(() -> new Alert(Alert.AlertType.INFORMATION, "Observed animal died", ButtonType.OK).show());
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
            worldMap.notifyObservers("Animal unhighlighted");
        }
    }
}