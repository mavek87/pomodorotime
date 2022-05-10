package com.matteoveroni.pomodorotime;

import com.matteoveroni.pomodorotime.configs.Config;
import com.matteoveroni.pomodorotime.configs.JsonConfigManager;
import javafx.application.Preloader;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class AppPreloader extends Preloader {

    @Override
    public void start(Stage stage) {
        Config config = JsonConfigManager.INSTANCE.readConfig();
        com.sun.glass.ui.Application.GetApplication().setName(config.getAppName());
    }
}