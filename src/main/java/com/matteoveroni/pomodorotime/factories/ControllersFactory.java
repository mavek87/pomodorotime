package com.matteoveroni.pomodorotime.factories;

import com.matteoveroni.pomodorotime.controllers.PomodoroController;
import com.matteoveroni.pomodorotime.services.ResourcesService;
import javafx.stage.Stage;
import javafx.util.Callback;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ControllersFactory implements Callback<Class<?>, Object> {

    private final Stage stage;
    private final ResourcesService resourcesService;

    public ControllersFactory(Stage stage, ResourcesService resourcesService) {
        this.stage = stage;
        this.resourcesService = resourcesService;
    }

    @Override
    public Object call(Class<?> controllerClass) {
        log.info("Building controller for => " + controllerClass);
        if (controllerClass.isAssignableFrom(PomodoroController.class)) {
            return new PomodoroController(stage, resourcesService);
        } else {
            throw new RuntimeException("Unknown controller class");
        }
    }
}
