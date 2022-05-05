package com.matteoveroni.pomodorotime.gui.controllers;

import com.matteoveroni.pomodorotime.gui.control.ControlAppFileMenu;
import com.matteoveroni.pomodorotime.gui.control.ControlSettings;
import com.matteoveroni.pomodorotime.gui.control.ControlPomodoro;
import com.matteoveroni.pomodorotime.services.ResourcesService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import java.net.URL;
import java.util.ResourceBundle;

public class AppViewController implements Initializable {

    @FXML private BorderPane pane_for_control_app_file_menu;
    @FXML private BorderPane pane_for_control_view;

    private final ResourcesService resourcesService;
    private final ControlPomodoro controlPomodoro;
    private final ControlSettings controlSettings;

    public AppViewController(ResourcesService resourcesService, ControlPomodoro controlPomodoro, ControlSettings controlSettings) {
        this.resourcesService = resourcesService;
        this.controlPomodoro = controlPomodoro;
        this.controlSettings = controlSettings;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pane_for_control_app_file_menu.setCenter(new ControlAppFileMenu(resourcesService, pane_for_control_view, controlPomodoro, controlSettings));
        pane_for_control_view.setCenter(controlPomodoro);
    }
}
