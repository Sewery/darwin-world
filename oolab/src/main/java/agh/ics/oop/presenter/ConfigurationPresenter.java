package agh.ics.oop.presenter;

import javafx.event.ActionEvent;

import java.io.IOException;

public class ConfigurationPresenter extends AppPresenter{

    public void onBackButtonClicked(ActionEvent actionEvent) throws IOException {
        super.changeScene("start.fxml", actionEvent, new StartPresenter());
    }
}

