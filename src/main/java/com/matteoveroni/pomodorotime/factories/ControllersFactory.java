package com.matteoveroni.pomodorotime.factories;

import com.matteoveroni.pomodorotime.configs.ConfigManager;
import com.matteoveroni.pomodorotime.gui.control.ControlPomodoro;
import com.matteoveroni.pomodorotime.gui.control.ControlSettings;
import com.matteoveroni.pomodorotime.gui.controllers.AppViewController;
import com.matteoveroni.pomodorotime.services.ResourcesService;
import javafx.util.Callback;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ControllersFactory implements Callback<Class<?>, Object> {

    private final ResourcesService resourcesService;
    private final ConfigManager configManager;
    private final ControlPomodoro controlPomodoro;
    private final ControlSettings controlSettings;

    public ControllersFactory(ResourcesService resourcesService, ConfigManager configManager, ControlPomodoro controlPomodoro, ControlSettings controlSettings) {
        this.resourcesService = resourcesService;
        this.configManager = configManager;
        this.controlPomodoro = controlPomodoro;
        this.controlSettings = controlSettings;
    }

    @Override
    public Object call(Class<?> controllerClass) {
        log.info("Building controller for => " + controllerClass);
        if (controllerClass.isAssignableFrom(AppViewController.class)) {
            return new AppViewController(resourcesService, controlPomodoro, controlSettings);
        } else {
            throw new RuntimeException("Unknown controller class");
        }
    }
}
