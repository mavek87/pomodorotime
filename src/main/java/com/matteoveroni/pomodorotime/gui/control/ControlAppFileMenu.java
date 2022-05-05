package com.matteoveroni.pomodorotime.gui.control;

import com.matteoveroni.pomodorotime.services.ResourcesService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import java.net.URL;
import java.util.ResourceBundle;

public class ControlAppFileMenu extends BorderPane implements LoadableControl, Initializable {

    private final BorderPane paneForAppControlView;
    private final ControlPomodoro controlPomodoro;
    private final ControlSettings controlSettings;

    public ControlAppFileMenu(ResourcesService resourcesService, BorderPane paneForAppControlView, ControlPomodoro controlPomodoro, ControlSettings controlSettings) {
        this.paneForAppControlView = paneForAppControlView;
        this.controlPomodoro = controlPomodoro;
        this.controlSettings = controlSettings;
        loadControl(resourcesService, Control.APP_FILE_MENU);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    void onActionPomodoro(ActionEvent event) {
        paneForAppControlView.setCenter(controlPomodoro);
    }

    @FXML
    void onActionSettings(ActionEvent event) {
        paneForAppControlView.setCenter(controlSettings);
    }

    @FXML
    void onActionAbout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Pomodoro-Time");
        alert.setContentText("Author: Matteo Veroni\nWebsite: www.matteoveroni.com");
//        alert.initOwner(paneForAppControlView.getScene().getWindow());
        alert.showAndWait();
    }

    @FXML
    void onActionExit(ActionEvent event) {
        Platform.exit();
    }
}