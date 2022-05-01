package com.matteoveroni.pomodorotime.services;

import java.net.URL;

public class ResourcesService {

    private static final Class<ResourcesService> clazz = ResourcesService.class;
    private static final String FXML_FOLDER = "/fxml/";
    private static final String ICONS_FOLDER = "/icons/";
    private static final String AUDIO_FOLDER = "/audio/";

    public URL getLogoIconURL() {
        return clazz.getResource(ICONS_FOLDER + "tomato.png");
    }

    public URL getPomodoroFXMLViewURL() {
        return clazz.getResource(FXML_FOLDER + "pomodoro.fxml");
    }

    public URL getSettingsFXMLViewURL() {
        return clazz.getResource(FXML_FOLDER + "settings.fxml");
    }

    public URL getAlarmAudioURL() {
        return clazz.getResource(AUDIO_FOLDER + "alarm.mp3");
    }
}
