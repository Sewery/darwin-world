package agh.ics.oop.presenter;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

public class AppPresenter {
    protected Scene scene;
    protected Stage stage;
    protected void alertError(Exception err) {
            System.err.println(err.getMessage());
            new Alert(Alert.AlertType.ERROR, err.getMessage(), ButtonType.OK).show();
    }
    protected void infoAlert(String message) {
        System.out.println(message);
        new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK).show();
    }
    public void changeScene(String newScene, ActionEvent actionEvent, AppPresenter presenter) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource(newScene));
        BorderPane viewRoot = loader.load();
        presenter = loader.getController();

        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(viewRoot);
        stage.setScene(scene);
        stage.minWidthProperty().bind(viewRoot.minWidthProperty());
        stage.minHeightProperty().bind(viewRoot.minHeightProperty());
        stage.show();
    }
    protected Image loadImageFromAssets(int width, int height, String fileName){
        Image image = null;
        try (InputStream input = getClass().getResourceAsStream("/assets/"+fileName)) {
            if (input == null) {
                throw new IOException("Resource not found: "+fileName);
            }
            image = new Image(input, width, height, true, true);
        } catch (IOException e) {
            System.err.println("Could not load image "+fileName+": " + e.getMessage());
        }
        return image;
    }
}
