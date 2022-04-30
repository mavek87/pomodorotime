package com.matteoveroni.pomodorotime;

import com.matteoveroni.pomodorotime.configs.Config;
import com.matteoveroni.pomodorotime.producers.ConfigProducer;
import javafx.application.Preloader;
import javafx.stage.Stage;

public class AppPreloader extends Preloader {

    @Override
    public void start(Stage stage) throws Exception {
        ConfigProducer configProducer = App.context.select(ConfigProducer.class).get();
        Config config = configProducer.getConfig();
        com.sun.glass.ui.Application.GetApplication().setName(config.getAppName());
    }
}