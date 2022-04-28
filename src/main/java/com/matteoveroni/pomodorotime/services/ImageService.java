package com.matteoveroni.pomodorotime.services;

import javafx.scene.image.Image;
import java.net.URL;

public class ImageService {

    private static final ClassLoader classLoader = ClassLoader.getSystemClassLoader();

    private static final String ICONS_FOLDER_PATH = "icons/";

    public static final String APP_LOGO_ICON_PATH = ICONS_FOLDER_PATH + "tomato.png";
    public static final URL APP_LOGO_ICON_URL = classLoader.getResource(APP_LOGO_ICON_PATH);
    public static final Image APP_LOGO_ICON_IMAGE = new Image(classLoader.getResourceAsStream(APP_LOGO_ICON_PATH));;

}
