package com.matteoveroni.pomodorotime;

import com.google.gson.Gson;
import com.matteoveroni.pomodorotime.configs.Config;
import javafx.application.Preloader;
import javafx.stage.Stage;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AppPreloader extends Preloader {

    @Override
    public void start(Stage stage) throws Exception {
        // Duplicated code and duplicate instance of gson... a refactor is needed
        Config config = new Gson().fromJson(Files.readString(Paths.get("config/config.json")), Config.class);
        com.sun.glass.ui.Application.GetApplication().setName(config.getAppName());
    }
}