package agh.ics.oop.presenter;

import agh.ics.oop.model.util.Configuration;
import agh.ics.oop.model.util.ConfigurationInvalidException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;

public class ConfigurationPresenter extends AppPresenter{


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
    private ToggleGroup animalBehaviourGroup,mapEdgesGroup;
    @FXML
    private void initialize() {

    }
    public void onBackButtonClicked(ActionEvent actionEvent) throws IOException {
        super.changeScene("start.fxml", actionEvent, new StartPresenter());
    }

    public void onSaveConfigButtonClicked(ActionEvent actionEvent) {
        System.out.println(getConfiguration());
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
                        .fromString(((RadioButton) animalBehaviourGroup
                                .getSelectedToggle())
                                .getText())
        );
    }
}

