package agh.ics.oop.presenter.components;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class HealthBarView {

    public AnchorPane createHealthBar(int currentEnergy, int maxEnergy, int cellSize) {
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
}
