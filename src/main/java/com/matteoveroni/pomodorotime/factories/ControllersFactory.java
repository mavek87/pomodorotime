package com.matteoveroni.pomodorotime.factories;

import com.matteoveroni.pomodorotime.configs.Config;
import com.matteoveroni.pomodorotime.controllers.PomodoroController;
import com.matteoveroni.pomodorotime.controllers.SettingsController;
import com.matteoveroni.pomodorotime.services.ResourcesService;
import javafx.stage.Stage;
import javafx.util.Callback;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ControllersFactory implements Callback<Class<?>, Object> {

    private final Stage stage;
    private final ResourcesService resourcesService;
    private final Config config;

    public ControllersFactory(Stage stage, ResourcesService resourcesService, Config config) {
        this.stage = stage;
        this.resourcesService = resourcesService;
        this.config = config;
    }

    @Override
    public Object call(Class<?> controllerClass) {
        log.info("Building controller for => " + controllerClass);
        if (controllerClass.isAssignableFrom(PomodoroController.class)) {
            return new PomodoroController(stage, resourcesService);
        } else if(controllerClass.isAssignableFrom(SettingsController.class)) {
            return new SettingsController(config);
        } else {
            throw new RuntimeException("Unknown controller class");
        }
    }
}
