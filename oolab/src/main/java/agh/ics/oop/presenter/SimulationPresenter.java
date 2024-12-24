package agh.ics.oop.presenter;

import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationEngine;
import agh.ics.oop.model.*;
import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.MapChangeListener;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.List;

import static java.lang.Math.max;


public class SimulationPresenter implements MapChangeListener {
    @FXML
    public TextField movesList;
    @FXML
    public Label moveDescription;
    @FXML
    private GridPane mapGrid;

    private WorldMap worldMap;

    private Boundary boundary;
    private int minX;
    private int minY;
    private int cellsInARow;
    private int cellsInAColumn;

    private final int maxMapSize = 300;
    private int cellSize = 20;

    public void setWorldMap(WorldMap worldMap) {
        this.worldMap = worldMap;
    }


    private void drawMap() {

        boundary = worldMap.getCurrentBounds();
        minX = boundary.lowerLeft().getX();
        minY = boundary.lowerLeft().getY();
        Vector2d subtracted = boundary.upperRight().subtract(boundary.lowerLeft());
        cellsInARow = subtracted.getX();
        cellsInAColumn = subtracted.getY();

        cellSize = max(maxMapSize/(max(cellsInAColumn, cellsInARow)+1), cellSize);


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

        // add elements
        for (int x = 1; x < cellsInARow+2; x++) {
            for (int y = cellsInAColumn+1; y >= 0; y--) {
                WorldElement worldElement = worldMap.objectAt(new Vector2d(x-1+minX, cellsInAColumn-y+minY));
                if (worldElement != null) {
                    Label mapElement = new Label(worldElement.toString());
                    mapGrid.add(mapElement, x, y+1);
                    GridPane.setHalignment(mapElement, HPos.CENTER);
                }
            }
        }

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

    @FXML
    public void onSimulationStartClicked() throws IllegalArgumentException {

            /*
            String[] args = movesList.getText().split(" ");
            List<MoveDirection> directions = OptionsParser.parse(args);
            List<Vector2d> positions = List.of(new Vector2d(2, 2), new Vector2d(3, 4));
            WorldMap map = new GrassField(10);
            map.addObserver(this);

            Simulation simulation = new Simulation(positions, directions, map);
            SimulationEngine engine = new SimulationEngine(List.of(simulation));
            engine.runAsync();
            */
    }

}
