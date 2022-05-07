package com.matteoveroni.pomodorotime.gui.control;

import com.dlsc.formsfx.model.validators.IntegerRangeValidator;
import com.dlsc.preferencesfx.PreferencesFx;
import com.dlsc.preferencesfx.model.Category;
import com.dlsc.preferencesfx.model.Group;
import com.dlsc.preferencesfx.model.Setting;
import com.matteoveroni.pomodorotime.configs.Config;
import com.matteoveroni.pomodorotime.configs.ConfigManager;
import com.matteoveroni.pomodorotime.gui.screen.ScreenResolution;
import com.matteoveroni.pomodorotime.services.localization.FXLocalizationService;
import com.matteoveroni.pomodorotime.services.localization.SupportedLocale;
import com.matteoveroni.pomodorotime.services.ResourcesService;
import com.matteoveroni.pomodorotime.utils.FXGraphicsUtils;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
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

    private final Stage stage;
    private final ConfigManager configManager;
    private FXLocalizationService localizationService;

    private final DoubleProperty pomodoroDurationProperty = new SimpleDoubleProperty(30);
    private final DoubleProperty pomodoroPauseProperty = new SimpleDoubleProperty(5);
    private final DoubleProperty pomodoroLongPauseProperty = new SimpleDoubleProperty(15);
    private final IntegerProperty numberOfSessionsBeforeLongPauseProperty = new SimpleIntegerProperty(4);

    private final BooleanProperty isPomodoroLoopProperty = new SimpleBooleanProperty(true);

    // Boolean
    private final BooleanProperty nightMode = new SimpleBooleanProperty(true);

    private final ObservableList<SupportedLocale> selectedLocale = FXCollections.observableArrayList(Arrays.asList(SupportedLocale.values()));
    private final ObjectProperty<SupportedLocale> selectedLocaleProperty = new SimpleObjectProperty<>(DEFAULT_LOCALE);

    // Combobox, Single Selection, with ObservableList
    private final ObservableList<ScreenResolution> resolutionItems = FXCollections.observableArrayList(Arrays.asList(ScreenResolution.values()));
    private final ObjectProperty<ScreenResolution> resolutionSelectionProperty = new SimpleObjectProperty<>(DEFAULT_SCREEN_SIZE_RESOLUTION);

    // Color
    private final ObjectProperty colorProperty = new SimpleObjectProperty<>(Color.PAPAYAWHIP);
    // Integer Range
    private final IntegerProperty fontSize = new SimpleIntegerProperty(12);

    private final Group timerGroup = Group.of("Pomodoro main settings",
            Setting.of("Pomodoro duration (minutes)", pomodoroDurationProperty, 1, 60, 0),
            Setting.of("Pause duration (minutes)", pomodoroPauseProperty, 1, 60, 0),
            Setting.of("Long pause duration (minutes)", pomodoroLongPauseProperty, 1, 60, 0),
            Setting.of("Sessions before a long pause", numberOfSessionsBeforeLongPauseProperty).validate(IntegerRangeValidator.atLeast(0, "Insert a positive number"))
    );
    private final Group timerSubGroup = Group.of("Pomodoro main settings",
            Setting.of("Pomodoro duration (minutes)", pomodoroDurationProperty, 1, 60, 0),
            Setting.of("Pause duration (minutes)", pomodoroPauseProperty, 1, 60, 1),
            Setting.of("Long pause duration (minutes)", pomodoroLongPauseProperty, 1, 60, 0),
            Setting.of("Sessions before a long pause", numberOfSessionsBeforeLongPauseProperty).validate(IntegerRangeValidator.atLeast(0, "Insert a positive number"))
    );

    private final Group timerRepetitionsGroup = Group.of("Pomodoro repetitions",
            Setting.of("Pomodoro loop", isPomodoroLoopProperty)
    );
    private final Group timerRepetitionsSubGroup = Group.of("Pomodoro repetitions",
            Setting.of("Pomodoro loop", isPomodoroLoopProperty)
    );

    private final Group screenResolutionGroup = Group.of("Screen resolution", Setting.of("Resolution", resolutionItems, resolutionSelectionProperty));
    private final Group screenResolutionSubGroup = Group.of("Screen resolution", Setting.of("Resolution", resolutionItems, resolutionSelectionProperty));

    private final Group themesGroup = Group.of("Themes", Setting.of("Night Mode", nightMode));
    private final Group themesSubGroup = Group.of("Themes", Setting.of("Night Mode", nightMode));

    private final Group textGroup = Group.of("Text", Setting.of("Font Color", colorProperty));
    private final Group textSubGroup = Group.of("Text", Setting.of("Font Size", fontSize, 6, 36));

    private final PreferencesFx preferencesFx = PreferencesFx.of(ControlSettings.class,
                    Category.of("Pomodoro timer", timerGroup, timerRepetitionsGroup)
                            .expand()
                            .subCategories(
                                    Category.of("Main settings", timerSubGroup),
                                    Category.of("Repetitions", timerRepetitionsSubGroup)
                            ),
                    Category.of("Graphics", screenResolutionGroup, themesGroup, textGroup)
                            .subCategories(
                                    Category.of("Screen", screenResolutionSubGroup),
                                    Category.of("Look and feel", themesSubGroup, textSubGroup)
                            ),
                    Category.of("Languages",
                            Group.of("Choose language",
                                    Setting.of("Language", selectedLocale, selectedLocaleProperty)
                            )
                    )
            )
            .persistWindowState(false)
            .saveSettings(false)
            .debugHistoryMode(true)
            .instantPersistent(true)
            .buttonsVisibility(false);

    public ControlSettings(Stage stage, ResourcesService resourcesService, ConfigManager configManager, FXLocalizationService localizationService) {
        this.stage = stage;
        this.configManager = configManager;
        this.localizationService = localizationService;
        loadControl(resourcesService, Control.SETTINGS);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.debug("INITIALIZE " + getClass().getSimpleName());
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
            stage.setWidth(newWindowWidth);
            stage.setHeight(newWindowHeight);
            FXGraphicsUtils.centerStage(stage);
        });

        final String language = startupConfig.getLanguage();
        selectedLocaleProperty.set(SupportedLocale.fromString(language).orElse(DEFAULT_LOCALE));
        selectedLocaleProperty.addListener((observable, oldValue, newValue) -> {
            log.debug("selectedLocaleProperty: {}", newValue);
            final Config currentConfig = configManager.readConfig();
            final Locale newLocale = newValue.getLocale();
            currentConfig.setLanguage(newLocale.toString());
            configManager.writeConfig(currentConfig);
            localizationService.selectedLocaleProperty().set(newLocale);
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

        isPomodoroLoopProperty.set(startupConfig.isPomodoroLoop());
        isPomodoroLoopProperty.addListener((observable, oldValue, newValue) -> {
            log.debug("isPomodoroLoopProperty: {}", newValue);
            final Config currentConfig = configManager.readConfig();
            currentConfig.setPomodoroLoop(newValue);
            configManager.writeConfig(currentConfig);
        });


    }

}