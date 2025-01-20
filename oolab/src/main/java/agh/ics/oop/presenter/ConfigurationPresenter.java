package agh.ics.oop.presenter;

import agh.ics.oop.core.Configuration;
import agh.ics.oop.core.ConfigurationValidator;
import agh.ics.oop.util.CSVConfigurationReader;
import agh.ics.oop.util.CSVWriter;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

import static agh.ics.oop.core.ConfigurationValidator.validate;

public class ConfigurationPresenter extends AppPresenter {
    private static Integer fileCounter = 0;
    @FXML
    private final ToggleGroup animalsBehaviourGroup = new ToggleGroup(), mapEdgesGroup = new ToggleGroup();
    @FXML
    private Spinner<Integer> height;
    @FXML
    private Spinner<Integer> width;
    @FXML
    private Spinner<Integer> initialNumberOfGrasses;
    @FXML
    private Spinner<Integer> numberOfNewGrassesEachDay;
    @FXML
    private Spinner<Integer> energyPerOneGrass;
    @FXML
    private Spinner<Integer> initialNumberOfAnimals;
    @FXML
    private Spinner<Integer> initialEnergyOfAnimals;
    @FXML
    private Spinner<Integer> energyToReproduce;
    @FXML
    private Spinner<Integer> minNumberOfMutations;
    @FXML
    private Spinner<Integer> maxNumberOfMutations;
    @FXML
    private Spinner<Integer> genotypeLength;
    @FXML
    private RadioButton forestedEquator, ageOfBurden;
    @FXML
    private RadioButton poles, globe;
    private  Configuration config;

    @FXML
    private void initialize() {
        height.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 20, 3));
        width.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 20, 3));
        initialNumberOfGrasses.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 2));
        energyPerOneGrass.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1));
        initialNumberOfAnimals.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 3));
        numberOfNewGrassesEachDay.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1));
        initialEnergyOfAnimals.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 6));
        energyToReproduce.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 3));
        minNumberOfMutations.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 1));
        maxNumberOfMutations.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 1));
        genotypeLength.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 5));

        forestedEquator.fire();
        forestedEquator.setToggleGroup(animalsBehaviourGroup);
        ageOfBurden.setToggleGroup(animalsBehaviourGroup);

        poles.setToggleGroup(mapEdgesGroup);
        globe.setToggleGroup(mapEdgesGroup);
        globe.fire();



    }

    public void onBackButtonClicked(ActionEvent actionEvent) throws IOException {
        super.changeScene("start.fxml", actionEvent, new StartPresenter());
    }

    public void onSaveConfigButtonClicked(ActionEvent actionEvent){
        Configuration config = loadConfiguration();
        try {
            validate(config);
        } catch (Exception e) {
            alertError(e);
            return;
        }
        this.config = config;
        CSVWriter.writeConfiguration(config, "config-" + fileCounter + ".csv", this::infoAlert);
        fileCounter++;

    }

    public void onStartButtonClicked(ActionEvent actionEvent) throws IOException {
        Configuration config = loadConfiguration();
        try {
            validate(config);
        } catch (Exception e) {
            alertError(e);
            return;
        }
        this.config = config;
        addStageSimulation("simulation.fxml");

    }

    public Configuration loadConfiguration() {
       return new Configuration(
                height.getValue(),
                width.getValue(),
                initialNumberOfGrasses.getValue(),
                numberOfNewGrassesEachDay.getValue(),
                energyPerOneGrass.getValue(),
                initialNumberOfAnimals.getValue(),
                initialEnergyOfAnimals.getValue(),
                energyToReproduce.getValue(),
                minNumberOfMutations.getValue(),
                maxNumberOfMutations.getValue(),
                genotypeLength.getValue(),
                Configuration.MapStrategy
                        .fromString(((RadioButton) mapEdgesGroup
                                .getSelectedToggle())
                                .getText()),
                Configuration.AnimalsBehaviourStrategy
                        .fromString(((RadioButton) animalsBehaviourGroup
                                .getSelectedToggle())
                                .getText()));

    }

    public void addStageSimulation(String newScene) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource(newScene));
        BorderPane viewRoot = loader.load();
        SimulationPresenter presenter = loader.getController();
        presenter.setConfiguration(this.config);

        Stage stage = new Stage();
        Scene scene = new Scene(viewRoot);
        stage.setScene(scene);
        stage.setTitle("Simulation runner");
        stage.minWidthProperty().bind(viewRoot.minWidthProperty());
        stage.minHeightProperty().bind(viewRoot.minHeightProperty());

        stage.setOnCloseRequest(event -> {
            Platform.exit();
        });
        stage.show();
    }

    public void onLoadSimulationClicked(ActionEvent actionEvent) {
        Configuration config = loadConfigurationFromFile();
        try {
            validate(config);
        } catch (Exception e) {
            alertError(e);
            return;
        }
        this.config = config;
    }
    public Configuration loadConfigurationFromFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Csv files", "*.csv"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        Configuration config =null;
        if (selectedFile != null) {
            config = CSVConfigurationReader.readConfiguration(
                    selectedFile, this::alertError,this::infoAlert
            );
        }
        return config;
    }
}

