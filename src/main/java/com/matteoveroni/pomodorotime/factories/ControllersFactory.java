package com.matteoveroni.pomodorotime.factories;

import com.matteoveroni.pomodorotime.configs.ConfigManager;
import com.matteoveroni.pomodorotime.gui.controllers.AppViewController;
import com.matteoveroni.pomodorotime.services.ResourcesService;
import javafx.stage.Stage;
import javafx.util.Callback;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ControllersFactory implements Callback<Class<?>, Object> {

    private final Stage stage;
    private final ResourcesService resourcesService;
    private final ConfigManager configManager;

    public ControllersFactory(Stage stage, ResourcesService resourcesService, ConfigManager configManager) {
        this.stage = stage;
        this.resourcesService = resourcesService;
        this.configManager = configManager;
    }

    @Override
    public Object call(Class<?> controllerClass) {
        log.info("Building controller for => " + controllerClass);
        if (controllerClass.isAssignableFrom(AppViewController.class)) {
            return new AppViewController(stage, resourcesService, configManager);
        } else {
            throw new RuntimeException("Unknown controller class");
        }
    }
}
