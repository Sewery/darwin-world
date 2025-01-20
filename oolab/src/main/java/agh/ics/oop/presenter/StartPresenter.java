package agh.ics.oop.presenter;

import agh.ics.oop.core.AppState;
import agh.ics.oop.core.Configuration;
import agh.ics.oop.core.ConfigurationInvalidException;
import agh.ics.oop.core.ConfigurationLoader;
import agh.ics.oop.util.CSVConfigurationReader;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class StartPresenter extends AppPresenter {

    public void onCreateNewSimulationClicked(ActionEvent actionEvent) throws IOException {
        super.changeScene("configuration.fxml", actionEvent, new ConfigurationPresenter());
    }

//    public void onLoadSimulationClicked(ActionEvent actionEvent) {
//        Configuration config = loadConfiguration();
//        try {
//            validate(config);
//        } catch (Exception e) {
//            alertError(e);
//            return;
//        }
//        AppState.getInstance().setConfig(config);
//
//    }
//
//    public void onStartClicked(ActionEvent actionEvent)  throws IOException{
//        if( AppState.getInstance().getConfig() != null) {
//            addStageSimulation("simulation.fxml");
//        }else{
//            alertError(new ConfigurationInvalidException("No configuration found"));
//        }
//    }
//    public void addStageSimulation(String newScene) throws IOException {
//
//        FXMLLoader loader = new FXMLLoader();
//        loader.setLocation(getClass().getClassLoader().getResource(newScene));
//        BorderPane viewRoot = loader.load();
//        SimulationPresenter presenter = loader.getController();
//        presenter.setConfiguration(AppState.getInstance().getConfig());
//
//        Stage stage =new Stage();
//        Scene scene = new Scene(viewRoot);
//        stage.setScene(scene);
//        stage.setTitle("Simulation runner");
//        stage.minWidthProperty().bind(viewRoot.minWidthProperty());
//        stage.minHeightProperty().bind(viewRoot.minHeightProperty());
//        stage.show();
//    }
//
//    @Override
//    public Configuration loadConfiguration() {
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Open Resource File");
//        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Csv files", "*.csv"));
//        File selectedFile = fileChooser.showOpenDialog(stage);
//        Configuration config =null;
//        if (selectedFile != null) {
//            config = CSVConfigurationReader.readConfiguration(
//                    selectedFile, this::alertError,this::infoAlert
//            );
//        }
//        return config;
//    }
}
