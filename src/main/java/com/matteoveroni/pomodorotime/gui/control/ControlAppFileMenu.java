package com.matteoveroni.pomodorotime.gui.control;

import com.matteoveroni.pomodorotime.gui.controllers.AppViewController;
import com.matteoveroni.pomodorotime.services.ResourcesService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
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

    private final Stage stage;
    private final AppViewController appViewController;
    private final ControlPomodoro controlPomodoro;
    private final ControlSettings controlSettings;

    public ControlAppFileMenu(Stage stage, AppViewController appViewController, ResourcesService resourcesService, ControlPomodoro controlPomodoro, ControlSettings controlSettings) {
        this.stage = stage;
        this.appViewController = appViewController;
        this.controlPomodoro = controlPomodoro;
        this.controlSettings = controlSettings;
        loadControl(resourcesService, Control.APP_FILE_MENU);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.debug("INITIALIZE " + getClass().getSimpleName());
    }

    @FXML
    void onActionPomodoro(ActionEvent event) {
        appViewController.showNodeInView(controlPomodoro);
    }

    @FXML
    void onActionSettings(ActionEvent event) {
        appViewController.showNodeInView(controlSettings);
    }

    @FXML
    void onActionAbout(ActionEvent event) {
        appViewController.setOverlayPane(true);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initStyle(StageStyle.UTILITY);
        alert.setResizable(false);
        alert.setTitle("About");
        alert.setHeaderText("Pomodoro-Time");

        BorderPane pane = new BorderPane();
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

        pane.setCenter(vBox);

        DialogPane alertDialogPane = alert.getDialogPane();
        alertDialogPane.setContent(pane);
        alertDialogPane.getScene().getWindow().setOnCloseRequest(Event::consume);

        alert.initOwner(stage);
        alert.showAndWait();

        appViewController.setOverlayPane(false);
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