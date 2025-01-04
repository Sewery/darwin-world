package agh.ics.oop.presenter;

import agh.ics.oop.model.util.Configuration;
import agh.ics.oop.util.CSVReader;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
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
        Configuration configuration =null;
        if (selectedFile != null) {
            configuration = CSVReader.readConfiguration(
                    selectedFile,
                    (err)->{
                        System.err.println(err.getMessage());
                        new Alert(Alert.AlertType.ERROR, err.getMessage(), ButtonType.OK).show();
                    }
            );
        }
        if(configuration != null) {
            //Redirect to map
        }
    }
}
