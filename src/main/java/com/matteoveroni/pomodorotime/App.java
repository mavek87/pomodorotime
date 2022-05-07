package com.matteoveroni.pomodorotime;

import com.matteoveroni.pomodorotime.configs.Config;
import com.matteoveroni.pomodorotime.configs.ConfigManager;
import com.matteoveroni.pomodorotime.factories.ControllersFactory;
import com.matteoveroni.pomodorotime.gui.views.View;
import com.matteoveroni.pomodorotime.services.ResourcesService;
import com.matteoveroni.pomodorotime.singleton.ConfigManagerSingleton;
import com.matteoveroni.pomodorotime.utils.FXGraphicsUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lombok.extern.slf4j.Slf4j;
import java.util.Objects;

@Slf4j
public class App extends Application {

    private ResourcesService resourcesService;
    private ControllersFactory controllersFactory;
    private Stage stage;
    private ConfigManager configManager;
    private Config config;

    public static final void main(String... args) {
        System.setProperty("javafx.preloader", AppPreloader.class.getCanonicalName());
        Application.launch(App.class, args);
    }

    @Override
    public void init() {
        resourcesService = new ResourcesService();
        configManager = ConfigManagerSingleton.INSTANCE;
        config = configManager.readConfig();
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        this.controllersFactory = new ControllersFactory(stage, resourcesService, configManager);

        FXMLLoader fxmlLoader = new FXMLLoader(resourcesService.getFXMLViewURL(View.APP_VIEW.getFileName()));
        fxmlLoader.setControllerFactory(controllersFactory);
        Pane pane = fxmlLoader.load();

        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.setTitle(config.getAppName());
        stage.getIcons().add(new Image(Objects.requireNonNull(resourcesService.getLogoIconURL().openStream())));
        stage.setWidth(config.getWindowWidth());
        stage.setHeight(config.getWindowHeight());
        stage.setMaxHeight(Double.MAX_VALUE);
        stage.setMaxWidth(Double.MAX_VALUE);
        stage.addEventHandler(WindowEvent.WINDOW_SHOWN, event -> FXGraphicsUtils.centerStage(stage));
        stage.show();
//        FXTrayIcon icon = new FXTrayIcon(this.stage, resourcesService.getLogoIconURL());
//        icon.show();
    }
}
