package com.matteoveroni.pomodorotime.gui.control;

import com.matteoveroni.pomodorotime.services.ResourcesService;
import javafx.fxml.FXMLLoader;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.net.URL;

public interface LoadableControl {

    default void loadControl(ResourcesService resourcesService, Control control) {
        final URL fxmlControlURL = resourcesService.getFXMLControlURL(control.getFileName());
        LoggerFactory.getLogger(LoadableControl.class).info("Loading control: {}", fxmlControlURL);

        final FXMLLoader fxmlLoader = new FXMLLoader(fxmlControlURL);
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
