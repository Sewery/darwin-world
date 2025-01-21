package agh.ics.oop.presenter.view_components;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class HealthBarView extends AnchorPane {
    public AnchorPane createHealthBar(int currentEnergy, int maxEnergy, int cellSize) {
        int width = cellSize * 7 / 10;
        int height = cellSize / 10;

        Rectangle background = new Rectangle(width, height);
        background.setFill(Color.DARKGRAY);
        background.setArcWidth(height);
        background.setArcHeight(height);

        Rectangle health = drawHealthRectangle(width,height,currentEnergy, maxEnergy);
        Rectangle border = drawBorderRectangle(width, height);

        AnchorPane healthBar = new AnchorPane();
        healthBar.setPrefSize(width, height);

        //adding background
        addingElement(healthBar,background,border.getWidth(),cellSize);
        //adding health
        addingElement(healthBar,health,border.getWidth(),cellSize);
        //adding border
        addingElement(healthBar,border,border.getWidth(),cellSize);

        return healthBar;
    }
    private <T extends Node> void addingElement(AnchorPane anchorPane, T element,double elementWidth, double cellSize) {
        anchorPane.getChildren().add(element);
        AnchorPane.setTopAnchor(element, 0.0);
        AnchorPane.setLeftAnchor(element, (cellSize-elementWidth)/2);
    }
    private Rectangle drawBorderRectangle(int width, int height) {
        Rectangle border = new Rectangle(width, height);
        border.setFill(Color.TRANSPARENT);
        border.setStroke(Color.BLACK);
        border.setStrokeWidth((double) height / 5);
        border.setArcWidth(height);
        border.setArcHeight(height);
        return border;
    }
    private Rectangle drawHealthRectangle(int width, int height, int currentEnergy, int maxEnergy) {
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
        return health;
    }
}
