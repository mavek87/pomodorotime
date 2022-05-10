package com.matteoveroni.pomodorotime.gui.control;

import com.dlsc.formsfx.model.util.ResourceBundleService;
import com.dlsc.formsfx.model.validators.IntegerRangeValidator;
import com.dlsc.preferencesfx.PreferencesFx;
import com.dlsc.preferencesfx.model.Category;
import com.dlsc.preferencesfx.model.Group;
import com.dlsc.preferencesfx.model.Setting;
import com.matteoveroni.pomodorotime.Settings;
import com.matteoveroni.pomodorotime.configs.Config;
import com.matteoveroni.pomodorotime.configs.ConfigManager;
import com.matteoveroni.pomodorotime.services.ResourcesService;
import com.matteoveroni.pomodorotime.services.localization.FXLocalizationService;
import com.matteoveroni.pomodorotime.utils.FXGraphicsUtils;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import static com.matteoveroni.pomodorotime.utils.Constants.LocalizationKeys.*;

@Slf4j
public class ControlSettings extends BorderPane implements Initializable, LoadableControl {

    // Color
//    private final ObjectProperty colorProperty = new SimpleObjectProperty<>(Color.PAPAYAWHIP);
    // Integer Range
//    private final IntegerProperty fontSize = new SimpleIntegerProperty(12);
    //    private final BooleanProperty nightMode = new SimpleBooleanProperty(true);
    private final Stage stage;
    private final ConfigManager configManager;
    private final Settings settings;
    private final FXLocalizationService localizationService;
    private final ResourceBundleService resourceBundleService;

//    private final Group themesGroup = Group.of("Themes", Setting.of("Night Mode", nightMode));
//    private final Group themesSubGroup = Group.of("Themes", Setting.of("Night Mode", nightMode));
//
//    private final Group textGroup = Group.of("Text", Setting.of("Font Color", colorProperty));
//    private final Group textSubGroup = Group.of("Text", Setting.of("Font Size", fontSize, 6, 36));

    public ControlSettings(Stage stage, ResourcesService resourcesService, ConfigManager configManager, Settings settings, FXLocalizationService localizationService, ResourceBundleService resourceBundleService) {
        this.stage = stage;
        this.configManager = configManager;
        this.settings = settings;
        this.localizationService = localizationService;
        this.resourceBundleService = resourceBundleService;
        loadControl(resourcesService, Control.SETTINGS);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.debug("INITIALIZE " + getClass().getSimpleName());

        final PreferencesFx preferencesFx = buildPreferencesFX();
        setCenter(preferencesFx.getView());

        settings.resolutionSelectionProperty().addListener((observable, oldValue, newValue) -> {
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
        settings.selectedLocaleProperty().addListener((observable, oldValue, newValue) -> {
            log.debug("selectedLocaleProperty: {}", newValue);
            final Config currentConfig = configManager.readConfig();
            final Locale newLocale = newValue.getLocale();
            localizationService.selectedLocaleProperty().set(newLocale);
            currentConfig.setLanguage(newLocale.toString());
            configManager.writeConfig(currentConfig);
        });
        settings.pomodoroDurationProperty().addListener((observable, oldValue, newValue) -> {
            log.debug("pomodoroDurationProperty: {}", newValue);
            final Config currentConfig = configManager.readConfig();
            currentConfig.setPomodoroDuration(newValue.intValue());
            configManager.writeConfig(currentConfig);
        });

        settings.pomodoroPauseProperty().addListener((observable, oldValue, newValue) -> {
            log.debug("pomodoroPauseProperty: {}", newValue);
            final Config currentConfig = configManager.readConfig();
            currentConfig.setPomodoroPauseDuration(newValue.intValue());
            configManager.writeConfig(currentConfig);
        });
        settings.pomodoroLongPauseProperty().addListener((observable, oldValue, newValue) -> {
            log.debug("pomodoroLongPauseProperty: {}", newValue);
            final Config currentConfig = configManager.readConfig();
            currentConfig.setPomodoroLongPauseDuration(newValue.intValue());
            configManager.writeConfig(currentConfig);
        });
        settings.numberOfSessionsBeforeLongPauseProperty().addListener((observable, oldValue, newValue) -> {
            log.debug("numberOfSessionsBeforeLongPauseProperty: {}", newValue);
            final Config currentConfig = configManager.readConfig();
            currentConfig.setNumberOfSessionBeforeLongPause(newValue.intValue());
            configManager.writeConfig(currentConfig);
        });
        settings.playAlarmSoundOnCompletionProperty().addListener((observable, oldValue, newValue) -> {
            log.debug("playAlarmSoundOnCompletionProperty: {}", newValue);
            final Config currentConfig = configManager.readConfig();
            currentConfig.setPlayAlarmSoundOnCompletion(newValue);
            configManager.writeConfig(currentConfig);
        });
        settings.allowInterruptPomodoroProperty().addListener((observable, oldValue, newValue) -> {
            log.debug("allowInterruptPomodoroProperty: {}", newValue);
            final Config currentConfig = configManager.readConfig();
            currentConfig.setAllowInterruptPomodoro(newValue);
            configManager.writeConfig(currentConfig);
        });
        settings.allowAbortPomodoroProperty().addListener((observable, oldValue, newValue) -> {
            log.debug("allowAbortPomodoroProperty: {}", newValue);
            final Config currentConfig = configManager.readConfig();
            currentConfig.setAllowAbortPomodoro(newValue);
            configManager.writeConfig(currentConfig);
        });
        settings.isPomodoroLoopProperty().addListener((observable, oldValue, newValue) -> {
            log.debug("isPomodoroLoopProperty: {}", newValue);
            final Config currentConfig = configManager.readConfig();
            currentConfig.setPomodoroLoop(newValue);
            configManager.writeConfig(currentConfig);
        });
        settings.isFullScreenPauseAlertOnProperty().addListener((observable, oldValue, newValue) -> {
            log.debug("isFullScreenPauseAlertOnProperty: {}", newValue);
            final Config currentConfig = configManager.readConfig();
            currentConfig.setPomodoroPauseAlertFullscreen(newValue);
            configManager.writeConfig(currentConfig);
        });
        settings.allowInterruptPauseProperty().addListener((observable, oldValue, newValue) -> {
            log.debug("allowInterruptPauseProperty: {}", newValue);
            final Config currentConfig = configManager.readConfig();
            currentConfig.setAllowInterruptPause(newValue);
            configManager.writeConfig(currentConfig);
        });
        settings.allowAbortPauseProperty().addListener((observable, oldValue, newValue) -> {
            log.debug("allowAbortPauseProperty: {}", newValue);
            final Config currentConfig = configManager.readConfig();
            currentConfig.setAllowAbortPause(newValue);
            configManager.writeConfig(currentConfig);
        });
    }

    private PreferencesFx buildPreferencesFX() {
        // TODO: find a way to localize the tooltip error message
        final Group pomodoroGroup = Group.of(POMODORO_MAIN_SETTINGS,
                Setting.of(POMODORO_DURATION_MINUTES, settings.pomodoroDurationProperty(), 1, 60, 0),
                Setting.of(PAUSE_DURATION_MINUTES, settings.pomodoroPauseProperty(), 1, 60, 0),
                Setting.of(LONG_PAUSE_DURATION_MINUTES, settings.pomodoroLongPauseProperty(), 1, 60, 0),
                Setting.of(SESSIONS_BEFORE_LONG_PAUSE, settings.numberOfSessionsBeforeLongPauseProperty()).validate(IntegerRangeValidator.atLeast(0, "Insert a positive number")),
                Setting.of(ALLOW_INTERRUPT_POMODORO, settings.allowInterruptPomodoroProperty()),
                Setting.of(ALLOW_ABORT_POMODORO, settings.allowAbortPomodoroProperty()),
                Setting.of(PLAY_ALARM_SOUND_AFTER_COMPLETION, settings.playAlarmSoundOnCompletionProperty())
        );
        final Group pomodoroSubGroup = Group.of(POMODORO_MAIN_SETTINGS,
                Setting.of(POMODORO_DURATION_MINUTES, settings.pomodoroDurationProperty(), 1, 60, 0),
                Setting.of(PAUSE_DURATION_MINUTES, settings.pomodoroPauseProperty(), 1, 60, 1),
                Setting.of(LONG_PAUSE_DURATION_MINUTES, settings.pomodoroLongPauseProperty(), 1, 60, 0),
                Setting.of(SESSIONS_BEFORE_LONG_PAUSE, settings.numberOfSessionsBeforeLongPauseProperty()).validate(IntegerRangeValidator.atLeast(0, "Insert a positive number")),
                Setting.of(ALLOW_INTERRUPT_POMODORO, settings.allowInterruptPomodoroProperty()),
                Setting.of(ALLOW_ABORT_POMODORO, settings.allowAbortPomodoroProperty()),
                Setting.of(PLAY_ALARM_SOUND_AFTER_COMPLETION, settings.playAlarmSoundOnCompletionProperty())
        );

        final Group pomodoroRepetitionsGroup = Group.of(POMODORO_REPETITIONS, Setting.of(POMODORO_LOOP, settings.isPomodoroLoopProperty()));
        final Group pomodoroRepetitionsSubGroup = Group.of(POMODORO_REPETITIONS, Setting.of(POMODORO_LOOP, settings.isPomodoroLoopProperty()));

        final Group pomodoroPauseGroup = Group.of(POMODORO_PAUSE,
                Setting.of(ALLOW_INTERRUPT_PAUSE, settings.allowInterruptPauseProperty()),
                Setting.of(ALLOW_ABORT_PAUSE, settings.allowAbortPauseProperty()),
                Setting.of(FULLSCREEN_PAUSE_ALERT, settings.isFullScreenPauseAlertOnProperty())
        );
        final Group pomodoroPauseSubGroup = Group.of(POMODORO_PAUSE,
                Setting.of(ALLOW_INTERRUPT_PAUSE, settings.allowInterruptPauseProperty()),
                Setting.of(ALLOW_ABORT_PAUSE, settings.allowAbortPauseProperty()),
                Setting.of(FULLSCREEN_PAUSE_ALERT, settings.isFullScreenPauseAlertOnProperty())
        );

        final Group screenResolutionGroup = Group.of(SCREEN_RESOLUTION, Setting.of(RESOLUTION, settings.getResolutionItems(), settings.resolutionSelectionProperty()));
        final Group screenResolutionSubGroup = Group.of(SCREEN_RESOLUTION, Setting.of(RESOLUTION, settings.getResolutionItems(), settings.resolutionSelectionProperty()));

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
                                        Setting.of(LANGUAGE, settings.getSelectedLocale(), settings.selectedLocaleProperty())
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