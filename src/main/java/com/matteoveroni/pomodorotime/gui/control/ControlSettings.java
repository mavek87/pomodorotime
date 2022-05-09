package com.matteoveroni.pomodorotime.gui.control;

import com.dlsc.formsfx.model.util.ResourceBundleService;
import com.dlsc.formsfx.model.validators.IntegerRangeValidator;
import com.dlsc.preferencesfx.PreferencesFx;
import com.dlsc.preferencesfx.model.Category;
import com.dlsc.preferencesfx.model.Group;
import com.dlsc.preferencesfx.model.Setting;
import com.matteoveroni.pomodorotime.configs.Config;
import com.matteoveroni.pomodorotime.configs.ConfigManager;
import com.matteoveroni.pomodorotime.gui.screen.ScreenResolution;
import com.matteoveroni.pomodorotime.services.ResourcesService;
import com.matteoveroni.pomodorotime.services.localization.FXLocalizationService;
import com.matteoveroni.pomodorotime.services.localization.SupportedLocale;
import com.matteoveroni.pomodorotime.utils.FXGraphicsUtils;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;

@Slf4j
public class ControlSettings extends BorderPane implements Initializable, LoadableControl {

    private static final ScreenResolution DEFAULT_SCREEN_SIZE_RESOLUTION = ScreenResolution.RESOLUTION_1024x768;
    private static final SupportedLocale DEFAULT_LOCALE = SupportedLocale.US;

    private static final String POMODORO = "POMODORO";
    private static final String GRAPHICS = "GRAPHICS";
    private static final String LOOK_AND_FEEL = "LOOK_AND_FEEL";
    private static final String MAIN_SETTINGS = "MAIN_SETTINGS";
    private static final String REPETITIONS = "REPETITIONS";
    private static final String PAUSE_SETTINGS = "PAUSE_SETTINGS";
    private static final String SCREEN = "SCREEN";
    private static final String LANGUAGES = "LANGUAGES";
    private static final String CHOOSE_LANGUAGE = "CHOOSE_LANGUAGE";
    private static final String LANGUAGE = "LANGUAGE";
    private static final String POMODORO_MAIN_SETTINGS = "POMODORO_MAIN_SETTINGS";
    private static final String POMODORO_DURATION_MINUTES = "POMODORO_DURATION_MINUTES";
    private static final String PAUSE_DURATION_MINUTES = "PAUSE_DURATION_MINUTES";
    private static final String LONG_PAUSE_DURATION_MINUTES = "LONG_PAUSE_DURATION_MINUTES";
    private static final String SESSIONS_BEFORE_LONG_PAUSE = "SESSIONS_BEFORE_LONG_PAUSE";
    private static final String POMODORO_REPETITIONS = "POMODORO_REPETITIONS";
    private static final String POMODORO_LOOP = "POMODORO_LOOP";
    private static final String POMODORO_PAUSE = "POMODORO_PAUSE";
    private static final String ALLOW_INTERRUPT_POMODORO = "ALLOW_INTERRUPT_POMODORO";
    private static final String ALLOW_ABORT_POMODORO = "ALLOW_ABORT_POMODORO";
    private static final String FULLSCREEN_PAUSE_ALERT = "FULLSCREEN_PAUSE_ALERT";
    private static final String ALLOW_INTERRUPT_PAUSE = "ALLOW_INTERRUPT_PAUSE";
    private static final String ALLOW_ABORT_PAUSE = "ALLOW_ABORT_PAUSE";
    private static final String SCREEN_RESOLUTION = "SCREEN_RESOLUTION";
    private static final String RESOLUTION = "RESOLUTION";

    // Color
//    private final ObjectProperty colorProperty = new SimpleObjectProperty<>(Color.PAPAYAWHIP);
    // Integer Range
//    private final IntegerProperty fontSize = new SimpleIntegerProperty(12);
    //    private final BooleanProperty nightMode = new SimpleBooleanProperty(true);
    private final Stage stage;
    private final ConfigManager configManager;
    private final FXLocalizationService localizationService;
    private final ResourceBundleService resourceBundleService;
    private final DoubleProperty pomodoroDurationProperty = new SimpleDoubleProperty(30);
    private final DoubleProperty pomodoroPauseProperty = new SimpleDoubleProperty(5);
    private final DoubleProperty pomodoroLongPauseProperty = new SimpleDoubleProperty(15);
    private final IntegerProperty numberOfSessionsBeforeLongPauseProperty = new SimpleIntegerProperty(4);
    private final BooleanProperty isPomodoroLoopProperty = new SimpleBooleanProperty(true);
    private final BooleanProperty allowInterruptPomodoroProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty allowAbortPomodoroProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty isFullScreenPauseAlertOnProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty allowInterruptPauseProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty allowAbortPauseProperty = new SimpleBooleanProperty(false);
    private final ObservableList<SupportedLocale> selectedLocale = FXCollections.observableArrayList(Arrays.asList(SupportedLocale.values()));
    private final ObjectProperty<SupportedLocale> selectedLocaleProperty = new SimpleObjectProperty<>(DEFAULT_LOCALE);
    private final ObservableList<ScreenResolution> resolutionItems = FXCollections.observableArrayList(Arrays.asList(ScreenResolution.values()));
    private final ObjectProperty<ScreenResolution> resolutionSelectionProperty = new SimpleObjectProperty<>(DEFAULT_SCREEN_SIZE_RESOLUTION);

    // TODO: find a way to localize the tooltip error message
    private final Group pomodoroGroup = Group.of(POMODORO_MAIN_SETTINGS,
            Setting.of(POMODORO_DURATION_MINUTES, pomodoroDurationProperty, 1, 60, 0),
            Setting.of(PAUSE_DURATION_MINUTES, pomodoroPauseProperty, 1, 60, 0),
            Setting.of(LONG_PAUSE_DURATION_MINUTES, pomodoroLongPauseProperty, 1, 60, 0),
            Setting.of(SESSIONS_BEFORE_LONG_PAUSE, numberOfSessionsBeforeLongPauseProperty).validate(IntegerRangeValidator.atLeast(0, "Insert a positive number")),
            Setting.of(ALLOW_INTERRUPT_POMODORO, allowInterruptPomodoroProperty),
            Setting.of(ALLOW_ABORT_POMODORO, allowAbortPomodoroProperty)
    );
    private final Group pomodoroSubGroup = Group.of(POMODORO_MAIN_SETTINGS,
            Setting.of(POMODORO_DURATION_MINUTES, pomodoroDurationProperty, 1, 60, 0),
            Setting.of(PAUSE_DURATION_MINUTES, pomodoroPauseProperty, 1, 60, 1),
            Setting.of(LONG_PAUSE_DURATION_MINUTES, pomodoroLongPauseProperty, 1, 60, 0),
            Setting.of(SESSIONS_BEFORE_LONG_PAUSE, numberOfSessionsBeforeLongPauseProperty).validate(IntegerRangeValidator.atLeast(0, "Insert a positive number")),
            Setting.of(ALLOW_INTERRUPT_POMODORO, allowInterruptPomodoroProperty),
            Setting.of(ALLOW_ABORT_POMODORO, allowAbortPomodoroProperty)
    );

    private final Group pomodoroRepetitionsGroup = Group.of(POMODORO_REPETITIONS, Setting.of(POMODORO_LOOP, isPomodoroLoopProperty));
    private final Group pomodoroRepetitionsSubGroup = Group.of(POMODORO_REPETITIONS, Setting.of(POMODORO_LOOP, isPomodoroLoopProperty));

    private final Group pomodoroPauseGroup = Group.of(POMODORO_PAUSE,
            Setting.of(ALLOW_INTERRUPT_PAUSE, allowInterruptPauseProperty),
            Setting.of(ALLOW_ABORT_PAUSE, allowAbortPauseProperty),
            Setting.of(FULLSCREEN_PAUSE_ALERT, isFullScreenPauseAlertOnProperty)
    );
    private final Group pomodoroPauseSubGroup = Group.of(POMODORO_PAUSE,
            Setting.of(ALLOW_INTERRUPT_PAUSE, allowInterruptPauseProperty),
            Setting.of(ALLOW_ABORT_PAUSE, allowAbortPauseProperty),
            Setting.of(FULLSCREEN_PAUSE_ALERT, isFullScreenPauseAlertOnProperty)
    );

    private final Group screenResolutionGroup = Group.of(SCREEN_RESOLUTION, Setting.of(RESOLUTION, resolutionItems, resolutionSelectionProperty));
    private final Group screenResolutionSubGroup = Group.of(SCREEN_RESOLUTION, Setting.of(RESOLUTION, resolutionItems, resolutionSelectionProperty));

//    private final Group themesGroup = Group.of("Themes", Setting.of("Night Mode", nightMode));
//    private final Group themesSubGroup = Group.of("Themes", Setting.of("Night Mode", nightMode));
//
//    private final Group textGroup = Group.of("Text", Setting.of("Font Color", colorProperty));
//    private final Group textSubGroup = Group.of("Text", Setting.of("Font Size", fontSize, 6, 36));

    public ControlSettings(Stage stage, ResourcesService resourcesService, ConfigManager configManager, FXLocalizationService localizationService, ResourceBundleService resourceBundleService) {
        this.stage = stage;
        this.configManager = configManager;
        this.localizationService = localizationService;
        this.resourceBundleService = resourceBundleService;
        loadControl(resourcesService, Control.SETTINGS);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.debug("INITIALIZE " + getClass().getSimpleName());

        final PreferencesFx preferencesFx = buildPreferencesFX();
        setCenter(preferencesFx.getView());

        final Config startupConfig = configManager.readConfig();

        final double windowWidth = startupConfig.getWindowWidth();
        final double windowHeight = startupConfig.getWindowHeight();
        resolutionSelectionProperty.set(ScreenResolution.fromSize(windowWidth, windowHeight).orElse(DEFAULT_SCREEN_SIZE_RESOLUTION));
        resolutionSelectionProperty.addListener((observable, oldValue, newValue) -> {
            log.debug("resolutionSelectionProperty: {}", newValue);
            final Config currentConfig = configManager.readConfig();
            final double newWindowWidth = newValue.getWidth();
            final double newWindowHeight = newValue.getHeight();
            currentConfig.setWindowWidth(newWindowWidth);
            currentConfig.setWindowHeight(newWindowHeight);
            configManager.writeConfig(currentConfig);
            //TODO: the next line fix a bug after screen resizing, except for the resolution preference view...
            preferencesFx.getView().setPrefSize(newWindowWidth, newWindowHeight);
            stage.setWidth(newWindowWidth);
            stage.setHeight(newWindowHeight);
            FXGraphicsUtils.centerStage(stage);
        });

        final String language = startupConfig.getLanguage();
        final SupportedLocale supportedLocale = SupportedLocale.fromString(language).orElse(DEFAULT_LOCALE);
        localizationService.selectedLocaleProperty().set(supportedLocale.getLocale());
        selectedLocaleProperty.set(supportedLocale);

        selectedLocaleProperty.addListener((observable, oldValue, newValue) -> {
            log.debug("selectedLocaleProperty: {}", newValue);
            final Config currentConfig = configManager.readConfig();
            final Locale newLocale = newValue.getLocale();
            localizationService.selectedLocaleProperty().set(newLocale);
            currentConfig.setLanguage(newLocale.toString());
            configManager.writeConfig(currentConfig);
        });

        pomodoroDurationProperty.set(startupConfig.getPomodoroDuration());
        pomodoroDurationProperty.addListener((observable, oldValue, newValue) -> {
            log.debug("pomodoroDurationProperty: {}", newValue);
            final Config currentConfig = configManager.readConfig();
            currentConfig.setPomodoroDuration(newValue.intValue());
            configManager.writeConfig(currentConfig);
        });

        pomodoroPauseProperty.set(startupConfig.getPomodoroPauseDuration());
        pomodoroPauseProperty.addListener((observable, oldValue, newValue) -> {
            log.debug("pomodoroPauseProperty: {}", newValue);
            final Config currentConfig = configManager.readConfig();
            currentConfig.setPomodoroPauseDuration(newValue.intValue());
            configManager.writeConfig(currentConfig);
        });

        pomodoroLongPauseProperty.set(startupConfig.getPomodoroLongPauseDuration());
        pomodoroLongPauseProperty.addListener((observable, oldValue, newValue) -> {
            log.debug("pomodoroLongPauseProperty: {}", newValue);
            final Config currentConfig = configManager.readConfig();
            currentConfig.setPomodoroLongPauseDuration(newValue.intValue());
            configManager.writeConfig(currentConfig);
        });

        numberOfSessionsBeforeLongPauseProperty.set(startupConfig.getNumberOfSessionBeforeLongPause());
        numberOfSessionsBeforeLongPauseProperty.addListener((observable, oldValue, newValue) -> {
            log.debug("numberOfSessionsBeforeLongPauseProperty: {}", newValue);
            final Config currentConfig = configManager.readConfig();
            currentConfig.setNumberOfSessionBeforeLongPause(newValue.intValue());
            configManager.writeConfig(currentConfig);
        });

        allowInterruptPomodoroProperty.set(startupConfig.isAllowInterruptPomodoro());
        allowInterruptPomodoroProperty.addListener((observable, oldValue, newValue) -> {
            log.debug("allowInterruptPomodoroProperty: {}", newValue);
            final Config currentConfig = configManager.readConfig();
            currentConfig.setAllowInterruptPomodoro(newValue);
            configManager.writeConfig(currentConfig);
        });

        allowAbortPomodoroProperty.set(startupConfig.isAllowAbortPomodoro());
        allowAbortPomodoroProperty.addListener((observable, oldValue, newValue) -> {
            log.debug("allowAbortPomodoroProperty: {}", newValue);
            final Config currentConfig = configManager.readConfig();
            currentConfig.setAllowAbortPomodoro(newValue);
            configManager.writeConfig(currentConfig);
        });

        isPomodoroLoopProperty.set(startupConfig.isPomodoroLoop());
        isPomodoroLoopProperty.addListener((observable, oldValue, newValue) -> {
            log.debug("isPomodoroLoopProperty: {}", newValue);
            final Config currentConfig = configManager.readConfig();
            currentConfig.setPomodoroLoop(newValue);
            configManager.writeConfig(currentConfig);
        });

        isFullScreenPauseAlertOnProperty.set(startupConfig.isPomodoroPauseAlertFullscreen());
        isFullScreenPauseAlertOnProperty.addListener((observable, oldValue, newValue) -> {
            log.debug("isFullScreenPauseAlertOnProperty: {}", newValue);
            final Config currentConfig = configManager.readConfig();
            currentConfig.setPomodoroPauseAlertFullscreen(newValue);
            configManager.writeConfig(currentConfig);
        });

        allowInterruptPauseProperty.set(startupConfig.isAllowInterruptPause());
        allowInterruptPauseProperty.addListener((observable, oldValue, newValue) -> {
            log.debug("allowInterruptPauseProperty: {}", newValue);
            final Config currentConfig = configManager.readConfig();
            currentConfig.setAllowInterruptPause(newValue);
            configManager.writeConfig(currentConfig);
        });

        allowAbortPauseProperty.set(startupConfig.isAllowAbortPause());
        allowAbortPauseProperty.addListener((observable, oldValue, newValue) -> {
            log.debug("allowAbortPauseProperty: {}", newValue);
            final Config currentConfig = configManager.readConfig();
            currentConfig.setAllowAbortPause(newValue);
            configManager.writeConfig(currentConfig);
        });

    }

    private PreferencesFx buildPreferencesFX() {
        return PreferencesFx.of(ControlSettings.class,
                        Category.of(POMODORO, pomodoroGroup, pomodoroRepetitionsGroup, pomodoroPauseGroup)
                                .expand()
                                .subCategories(
                                        Category.of(MAIN_SETTINGS, pomodoroSubGroup),
                                        Category.of(REPETITIONS, pomodoroRepetitionsSubGroup),
                                        Category.of(PAUSE_SETTINGS, pomodoroPauseSubGroup)
                                ),
                        Category.of(GRAPHICS, screenResolutionGroup)
                                .subCategories(
                                        Category.of(SCREEN, screenResolutionSubGroup),
                                        Category.of(LOOK_AND_FEEL)
                                ),
                        Category.of(LANGUAGES,
                                Group.of(CHOOSE_LANGUAGE,
                                        Setting.of(LANGUAGE, selectedLocale, selectedLocaleProperty)
                                )
                        )
                )
                .i18n(resourceBundleService)
                .persistWindowState(false)
                .saveSettings(false)
                .debugHistoryMode(true)
                .instantPersistent(true)
                .buttonsVisibility(false);
    }

}