package com.matteoveroni.pomodorotime;

import com.matteoveroni.pomodorotime.configs.Config;
import com.matteoveroni.pomodorotime.configs.ConfigManager;
import com.matteoveroni.pomodorotime.configs.JsonConfigManager;
import javafx.application.Preloader;
import javafx.stage.Stage;

/**
 * Workaround to always set correctly the application name in all O.S. (Ubuntu included)
 * Otherwise in some O.S. the application title is incorrectly set to the main class complete package
 */
public final class AppPreloader extends Preloader {

    @Override
    public void start(Stage stage) {
        final ConfigManager configManager = JsonConfigManager.INSTANCE;
        final Config config = configManager.readConfig();
        com.sun.glass.ui.Application.GetApplication().setName(config.getAppName());
    }
}