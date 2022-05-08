package com.matteoveroni.pomodorotime.gui.controllers;

import com.matteoveroni.pomodorotime.configs.ConfigManager;
import com.matteoveroni.pomodorotime.gui.control.ControlAppFileMenu;
import com.matteoveroni.pomodorotime.gui.control.ControlPomodoro;
import com.matteoveroni.pomodorotime.gui.control.ControlSettings;
import com.matteoveroni.pomodorotime.gui.model.PomodoroModel;
import com.matteoveroni.pomodorotime.services.ResourcesService;
import com.matteoveroni.pomodorotime.services.localization.FXLocalizationService;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class AppViewController implements Initializable {

    @FXML private BorderPane root_pane;
    @FXML private StackPane stackpane;
    @FXML private BorderPane app_pane;
    @FXML private AnchorPane overlay_pane;
    @FXML private BorderPane pane_for_control_view;

    private final ResourcesService resourcesService;

    private ConfigManager configManager;
    private FXLocalizationService localizationService;
    private Stage stage;

    public AppViewController(Stage stage, ResourcesService resourcesService, ConfigManager configManager, FXLocalizationService localizationService) {
        this.stage = stage;
        this.resourcesService = resourcesService;
        this.configManager = configManager;
        this.localizationService = localizationService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        final ControlPomodoro controlPomodoro = new ControlPomodoro(stage, this, new PomodoroModel(configManager), resourcesService, configManager, localizationService);
        final ControlSettings controlSettings = new ControlSettings(stage, resourcesService, configManager, localizationService);
        final ControlAppFileMenu controlAppFileMenu = new ControlAppFileMenu(stage,this, resourcesService, controlPomodoro, controlSettings, localizationService);
        app_pane.setTop(controlAppFileMenu);
        pane_for_control_view.setCenter(controlPomodoro);
        setOverlayPane(false);
        stage.setOnCloseRequest(confirmCloseEventHandler);
    }

    public void showNodeInView(Node node) {
        pane_for_control_view.setCenter(node);
    }

    public void setOverlayPane(boolean enableOverlayPane) {
        if (enableOverlayPane) {
            overlay_pane.setVisible(true);
            overlay_pane.toFront();
            app_pane.toBack();
        } else {
            overlay_pane.setVisible(false);
            overlay_pane.toBack();
            app_pane.toFront();
        }
    }

    private final EventHandler<WindowEvent> confirmCloseEventHandler = event -> {
        setOverlayPane(true);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to quit?");
        alert.initStyle(StageStyle.UTILITY);
        alert.setResizable(false);
        alert.setTitle(configManager.readConfig().getAppName());
        alert.setHeaderText("Exit confirmation");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(stage);
        alert.getDialogPane().getScene().getWindow().setOnCloseRequest(Event::consume);
        Button exitButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
        exitButton.setText("Exit");
        Optional<ButtonType> closeButton = alert.showAndWait();
        if (!ButtonType.OK.equals(closeButton.get())) {
            event.consume();
        }
        setOverlayPane(false);
    };
}
