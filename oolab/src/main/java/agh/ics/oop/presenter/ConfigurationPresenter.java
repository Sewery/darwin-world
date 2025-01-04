package agh.ics.oop.presenter;

import agh.ics.oop.model.util.Configuration;
import agh.ics.oop.model.util.ConfigurationInvalidException;
import agh.ics.oop.service.CSVWriter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

public class ConfigurationPresenter extends AppPresenter{
    private static Integer counter = 0;
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
    private Button saveConfig;
    @FXML
    private RadioButton forestedEquator, ageOfBurden;
    @FXML
    private RadioButton poles,globe;
    @FXML
    private final ToggleGroup animalsBehaviourGroup =new ToggleGroup(), mapEdgesGroup = new ToggleGroup();
    @FXML
    private void initialize() {
        height.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(100,1000,200));
        width.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(100,1000,200));
        initialNumberOfGrasses.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(100,1000,200));
        energyPerOneGrass.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(100,1000,200));
        initialNumberOfAnimals.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(100,1000,200));
        numberOfNewGrassesEachDay.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(100,1000,200));
        initialEnergyOfAnimals.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(100,1000,200));
        energyToReproduce.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(100,1000,200));
        minNumberOfMutations.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(100,1000,200));
        maxNumberOfMutations.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(100,1000,200));
        genotypeLength.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(100,1000,200));

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

    public void onSaveConfigButtonClicked(ActionEvent actionEvent) {
        CSVWriter.writeConfiguration(getConfiguration(),"config-"+counter+".csv");
        counter++;
    }

    public void onStartButtonClicked(ActionEvent actionEvent) {
    }
    private void configurationValidation() throws ConfigurationInvalidException {
        if(false){
            throw new ConfigurationInvalidException("Max number of Mutations must be an positive integer");
        }
    }

    public Configuration getConfiguration(){
        try{
            configurationValidation();
        }catch (ConfigurationInvalidException e){
            new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK).show();
        }
        System.out.println(mapEdgesGroup.getToggles().toString());
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
                                .getText())
        );
    }
}

