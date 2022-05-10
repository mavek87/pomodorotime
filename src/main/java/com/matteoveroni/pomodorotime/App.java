package com.matteoveroni.pomodorotime;

import com.dlsc.formsfx.model.util.ResourceBundleService;
import com.matteoveroni.pomodorotime.configs.Config;
import com.matteoveroni.pomodorotime.configs.ConfigManager;
import com.matteoveroni.pomodorotime.configs.PropertiesConfigManager;
import com.matteoveroni.pomodorotime.factories.ControllersFactory;
import com.matteoveroni.pomodorotime.factories.FXLocalizationServiceFactory;
import com.matteoveroni.pomodorotime.gui.screen.ScreenResolution;
import com.matteoveroni.pomodorotime.gui.views.View;
import com.matteoveroni.pomodorotime.services.ResourcesService;
import com.matteoveroni.pomodorotime.services.localization.FXLocalizationService;
import com.matteoveroni.pomodorotime.services.localization.SupportedLocale;
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
import static com.matteoveroni.pomodorotime.Settings.DEFAULT_LOCALE;
import static com.matteoveroni.pomodorotime.Settings.DEFAULT_SCREEN_SIZE_RESOLUTION;

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
        configManager = PropertiesConfigManager.INSTANCE;
        localizationService = new FXLocalizationServiceFactory().produce();
        config = configManager.readConfig();
        settings = new Settings();
        initSettings();
        resourceBundleService = buildResourceBundleServicesAndBindItWithLocalizationService();
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
    }

    // TODO: extract in a class builder
    private void initSettings() {
        // TODO: distribute without a default locale in the config file. If the locale is not there try to use the system locale.
        // Otherwise if not found use the dafault; english (us)
        final String language = config.getLanguage();
        final SupportedLocale supportedLocale = SupportedLocale.fromString(language).orElse(DEFAULT_LOCALE);
        localizationService.selectedLocaleProperty().set(supportedLocale.getLocale());
        settings.setSelectedLocale(supportedLocale);

        final double windowWidth = config.getWindowWidth();
        final double windowHeight = config.getWindowHeight();
        settings.setResolutionSelection(ScreenResolution.fromSize(windowWidth, windowHeight).orElse(DEFAULT_SCREEN_SIZE_RESOLUTION));

        settings.setPomodoroDuration(config.getPomodoroDuration());
        settings.setPomodoroPause(config.getPomodoroPauseDuration());
        settings.setPomodoroLongPause(config.getPomodoroLongPauseDuration());
        settings.setNumberOfSessionsBeforeLongPause(config.getNumberOfSessionBeforeLongPause());
        settings.setPlayAlarmSoundOnCompletion(config.isPlayAlarmSoundOnCompletion());
        settings.setAllowInterruptPomodoro(config.isAllowInterruptPomodoro());
        settings.setAllowAbortPomodoro(config.isAllowAbortPomodoro());
        settings.setIsPomodoroLoop(config.isPomodoroLoop());
        settings.setIsFullScreenPauseAlertOn(config.isPomodoroPauseAlertFullscreen());
        settings.setAllowInterruptPause(config.isAllowInterruptPause());
        settings.setAllowAbortPause(config.isAllowAbortPause());
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
