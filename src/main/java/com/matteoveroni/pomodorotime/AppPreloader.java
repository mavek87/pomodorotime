package com.matteoveroni.pomodorotime;

import com.matteoveroni.pomodorotime.configs.Config;
import com.matteoveroni.pomodorotime.singleton.ConfigSingleton;
import javafx.application.Preloader;
import javafx.stage.Stage;

public class AppPreloader extends Preloader {

    @Override
    public void start(Stage stage) {
        Config config = ConfigSingleton.INSTANCE.getConfig();
        com.sun.glass.ui.Application.GetApplication().setName(config.getAppName());
    }
}