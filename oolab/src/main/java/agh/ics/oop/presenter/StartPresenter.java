package agh.ics.oop.presenter;


import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.io.IOException;

public class StartPresenter extends AppPresenter {
    @FXML
    private ImageView animatedSheepImage; // First animated ImageView

    @FXML
    private ImageView animatedGrassImage; // Second animated ImageView


    @FXML
    public void initialize() {

        // Load the firs image
        animatedSheepImage.setImage(loadImageFromAssets(120,120,"sheep_crown.png"));
        animatedSheepImage.setVisible(true); // Make the image visible

        // Load the second image
        animatedGrassImage.setImage(loadImageFromAssets(120,120,"grass.jpg"));
        animatedGrassImage.setVisible(true); // Make the image visible

        // Start animations for both images
        startImageAnimation(animatedGrassImage,-1); // Animate first image
        startImageAnimation(animatedSheepImage,.1); // Animate second image with different offset
    }

    private void startImageAnimation(ImageView imageView,double dir) {
        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.8), imageView);
        transition.setByY(30);
        transition.setByX(dir*10);
        transition.setCycleCount(TranslateTransition.INDEFINITE);
        transition.setAutoReverse(true);
        transition.play();
    }


    public void onCreateNewSimulationClicked(ActionEvent actionEvent) throws IOException {
        super.changeScene("configuration.fxml", actionEvent, new ConfigurationPresenter());
    }

}
