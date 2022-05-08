package com.matteoveroni.pomodorotime.factories;

import com.dlsc.formsfx.model.util.ResourceBundleService;
import com.matteoveroni.pomodorotime.configs.ConfigManager;
import com.matteoveroni.pomodorotime.gui.controllers.AppViewController;
import com.matteoveroni.pomodorotime.services.ResourcesService;
import com.matteoveroni.pomodorotime.services.localization.FXLocalizationService;
import javafx.stage.Stage;
import javafx.util.Callback;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ControllersFactory implements Callback<Class<?>, Object> {

    private final Stage stage;
    private final ResourcesService resourcesService;
    private final ConfigManager configManager;
    private final FXLocalizationService fxLocalizationService;
    private final ResourceBundleService resourceBundleService;

    public ControllersFactory(Stage stage, ResourcesService resourcesService, ConfigManager configManager, FXLocalizationService fxLocalizationService, ResourceBundleService resourceBundleService) {
        this.stage = stage;
        this.resourcesService = resourcesService;
        this.configManager = configManager;
        this.fxLocalizationService = fxLocalizationService;
        this.resourceBundleService = resourceBundleService;
    }

    @Override
    public Object call(Class<?> controllerClass) {
        log.info("Building controller for => " + controllerClass);
        if (controllerClass.isAssignableFrom(AppViewController.class)) {
            return new AppViewController(stage, resourcesService, configManager, fxLocalizationService, resourceBundleService);
        } else {
            throw new RuntimeException("Unknown controller class");
        }
    }
}
