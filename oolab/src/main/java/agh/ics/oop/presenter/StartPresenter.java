package agh.ics.oop.presenter;

import javafx.event.ActionEvent;

import java.io.IOException;

public class StartPresenter extends AppPresenter{

    public void onCreateNewSimulationClicked(ActionEvent actionEvent) throws IOException {
        super.changeScene("configuration.fxml", actionEvent, new ConfigurationPresenter());
    }
}
