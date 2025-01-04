package agh.ics.oop.presenter;

import agh.ics.oop.service.CSVReader;
import javafx.event.ActionEvent;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

public class StartPresenter extends AppPresenter{

    public void onCreateNewSimulationClicked(ActionEvent actionEvent) throws IOException {
        super.changeScene("configuration.fxml", actionEvent, new ConfigurationPresenter());
    }

    public void onLoadSimulationClicked(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Csv files", "*.csv"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            CSVReader.readConfiguration(selectedFile);
        }
    }
}
