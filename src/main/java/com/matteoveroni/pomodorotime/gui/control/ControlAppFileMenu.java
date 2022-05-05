package com.matteoveroni.pomodorotime.gui.control;

import com.matteoveroni.pomodorotime.services.ResourcesService;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.extern.slf4j.Slf4j;
import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;

@Slf4j
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
        alert.initStyle(StageStyle.UTILITY);
        alert.setResizable(false);
        alert.setTitle("About");
        alert.setHeaderText("Pomodoro-Time");

        BorderPane alertPane = new BorderPane();
        VBox vBox = new VBox();
        vBox.setSpacing(15);
        vBox.getChildren().add(new Label("Author: Matteo Veroni"));

        HBox hBox = new HBox();
        hBox.getChildren().add(new Label("Website:"));
        Hyperlink websiteHyperlink = new Hyperlink("https://github.com/mavek87/pomodorotime");
        websiteHyperlink.visitedProperty().addListener((observable, oldValue, newValue) -> {
            openWebPage(websiteHyperlink.getText());
        });
        hBox.getChildren().add(websiteHyperlink);
        vBox.getChildren().add(hBox);

        alertPane.setCenter(vBox);

        alert.getDialogPane().setContent(alertPane);

        alert.initOwner((Stage) paneForAppControlView.getScene().getWindow());
        alert.showAndWait();
    }

    @FXML
    void onActionExit(ActionEvent event) {
        Platform.exit();
    }

    public static void openWebPage(String url) {
        SwingUtilities.invokeLater(() -> {
            try {
                Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
                if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                    desktop.browse(new URI(url));
                }
            } catch (Exception e) {
                log.error("Error", e);
            }
        });
    }
}