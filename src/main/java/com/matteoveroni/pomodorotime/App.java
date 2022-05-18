package com.matteoveroni.pomodorotime;

import com.dlsc.formsfx.model.util.ResourceBundleService;
import com.matteoveroni.pomodorotime.builders.ResourceBundleServiceBuilder;
import com.matteoveroni.pomodorotime.builders.SettingsBuilder;
import com.matteoveroni.pomodorotime.configs.Config;
import com.matteoveroni.pomodorotime.configs.ConfigManager;
import com.matteoveroni.pomodorotime.configs.JsonConfigManager;
import com.matteoveroni.pomodorotime.factories.ControllersFactory;
import com.matteoveroni.pomodorotime.factories.FXLocalizationServiceFactory;
import com.matteoveroni.pomodorotime.gui.views.View;
import com.matteoveroni.pomodorotime.services.ResourcesService;
import com.matteoveroni.pomodorotime.services.localization.FXLocalizationService;
import com.matteoveroni.pomodorotime.utils.FXGraphicsUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;
import java.util.Objects;

@Slf4j
public final class App extends Application {

    private String version;
    private ResourcesService resourcesService;
    private ConfigManager configManager;
    private Settings settings;
    private Config config;
    private FXLocalizationService localizationService;
    private ResourceBundleService resourceBundleService;

    public static final void main(String... args) {
        System.setProperty("javafx.preloader", AppPreloader.class.getCanonicalName());
        Application.launch(App.class, args);
    }

    @Override
    public void init() {
        resourcesService = new ResourcesService();
        configManager = JsonConfigManager.INSTANCE;
        localizationService = new FXLocalizationServiceFactory().create();
        config = configManager.readConfig();
        settings = new SettingsBuilder(config, localizationService).build();
        resourceBundleService = new ResourceBundleServiceBuilder(localizationService).build();
        version = resourcesService.readVersion();
    }

    @Override
    public void start(Stage stage) throws Exception {
        final ControllersFactory controllersFactory = new ControllersFactory(stage, resourcesService, configManager, settings, localizationService, resourceBundleService);
        final Pane mainViewPane = loadFXMLMainView(controllersFactory);
        final Scene scene = new Scene(mainViewPane);
        stage.setScene(scene);
        stage.setTitle(config.getAppName() + " - " + version);
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

    public void stop() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> log.info("Closing the app")));
    }

    private Pane loadFXMLMainView(ControllersFactory controllersFactory) throws IOException {
        final FXMLLoader fxmlLoader = new FXMLLoader(resourcesService.getFXMLViewURL(View.APP_VIEW.getFileName()));
        fxmlLoader.setControllerFactory(controllersFactory);
        return fxmlLoader.load();
    }
}
