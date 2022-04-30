package com.matteoveroni.pomodorotime.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.net.URL;

@ApplicationScoped
public class ResourcesService {

    private static final String FXML_FOLDER = "";
    private static final String ICONS_FOLDER = "icons/";

    private final ClassLoader classLoader;

    @Inject
    public ResourcesService(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public URL getLogoIconURL() {
        return classLoader.getResource(ICONS_FOLDER + "tomato.png");
    }

    public URL getPomodoroFXMLViewURL() {
        return classLoader.getResource(FXML_FOLDER + "pomodoro.fxml");
    }
}
