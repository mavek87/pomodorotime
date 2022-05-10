package com.matteoveroni.pomodorotime.services;

import lombok.extern.slf4j.Slf4j;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

@Slf4j
public final class ResourcesService {

    private static final Class<ResourcesService> clazz = ResourcesService.class;
    private static final String ICONS_FOLDER = "/icons/";
    private static final String AUDIO_FOLDER = "/audio/";
    private static final String FXML_VIEWS_FOLDER = "/fxml/views/";
    private static final String FXML_CONTROLS_FOLDER = "/fxml/controls/";
    private static final String FXML_EXTENSION = ".fxml";
    private static final String VERSION_PROPS = "/version.properties";

    public String readVersion() {
        String version;
        try (InputStream inputStream = new FileInputStream(getClass().getResource(VERSION_PROPS).getFile())) {
            Properties versionProps = new Properties();
            versionProps.load(inputStream);
            version = versionProps.getProperty("version", null);
        } catch (IOException ex) {
            log.error("Error", ex);
            throw new RuntimeException(ex);
        }
        return version;
    }

    public URL getLogoIconURL() {
        return clazz.getResource(ICONS_FOLDER + "tomato.png");
    }

    public URL getAlarmAudioURL() {
        return clazz.getResource(AUDIO_FOLDER + "alarm.mp3");
    }

    public URL getFXMLViewURL(String fxmlViewFileName) {
        fxmlViewFileName = addFXMLExtensionIfNeeded(fxmlViewFileName);
        String fxmlViewPath = FXML_VIEWS_FOLDER + fxmlViewFileName;
        log.debug("Loading view {}", fxmlViewPath);
        return clazz.getResource(fxmlViewPath);
    }

    public URL getFXMLControlURL(String fxmlControlFileName) {
        fxmlControlFileName = addFXMLExtensionIfNeeded(fxmlControlFileName);
        String fxmlControlPath = FXML_CONTROLS_FOLDER + fxmlControlFileName;
        log.debug("Loading control {}", fxmlControlPath);
        return clazz.getResource(fxmlControlPath);
    }

    private String addFXMLExtensionIfNeeded(String fxmlFileName) {
        if (!fxmlFileName.endsWith(FXML_EXTENSION)) {
            fxmlFileName = fxmlFileName + FXML_EXTENSION;
        }
        return fxmlFileName;
    }
}
