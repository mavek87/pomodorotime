package com.matteoveroni.pomodorotime;

import com.dlsc.formsfx.model.util.ResourceBundleService;
import com.dlsc.formsfx.model.util.TranslationService;
import com.matteoveroni.pomodorotime.configs.Config;
import com.matteoveroni.pomodorotime.configs.ConfigManager;
import com.matteoveroni.pomodorotime.factories.ControllersFactory;
import com.matteoveroni.pomodorotime.gui.views.View;
import com.matteoveroni.pomodorotime.services.ResourcesService;
import com.matteoveroni.pomodorotime.factories.FXLocalizationServiceFactory;
import com.matteoveroni.pomodorotime.services.localization.FXLocalizationService;
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
import java.io.IOException;
import java.util.Objects;
import java.util.ResourceBundle;

@Slf4j
public final class App extends Application {

    private ResourcesService resourcesService;
    private ConfigManager configManager;
    private Config config;
    private FXLocalizationService localizationService;

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

    public class A extends TranslationService {

        @Override
        public String translate(String key) {
            return null;
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.localizationService = new FXLocalizationServiceFactory().produce();
        final ResourceBundleService resourceBundleService = buildResourceBundleServicesAndBindItWithLocalizationService();
        final ControllersFactory controllersFactory = new ControllersFactory(stage, resourcesService, configManager, localizationService, resourceBundleService);
        final Pane mainViewPane = loadFXMLMainView(controllersFactory);
        final Scene scene = new Scene(mainViewPane);
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

    public void stop() {
    }

    private ResourceBundleService buildResourceBundleServicesAndBindItWithLocalizationService() {
        final ResourceBundle resourceBundle = localizationService.getCurrentlyUsedResourceBundle().orElseThrow(() -> new IllegalStateException("Error, current resource bundle is not set"));
        final ResourceBundleService resourceBundleService = new ResourceBundleService(resourceBundle);
        localizationService.selectedLocaleProperty().addListener((observable, oldValue, newValue) ->
                resourceBundleService.changeLocale(localizationService.getCurrentlyUsedResourceBundle().get())
        );
        return resourceBundleService;
    }

    private Pane loadFXMLMainView(ControllersFactory controllersFactory) throws IOException {
        final FXMLLoader fxmlLoader = new FXMLLoader(resourcesService.getFXMLViewURL(View.APP_VIEW.getFileName()));
        fxmlLoader.setControllerFactory(controllersFactory);
        return fxmlLoader.load();
    }
}
