package agh.ics.oop.presenter;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class AppPresenter {
    protected Scene scene;
    protected Stage stage;
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
}
