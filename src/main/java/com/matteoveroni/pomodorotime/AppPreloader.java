package com.matteoveroni.pomodorotime;

import javafx.application.Preloader;
import javafx.stage.Stage;

public class AppPreloader extends Preloader {

    @Override
    public void start(Stage stage) throws Exception {
        com.sun.glass.ui.Application.GetApplication().setName(App.APP_TITLE);
    }
}